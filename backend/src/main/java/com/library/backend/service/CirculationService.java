package com.library.backend.service;

import com.library.backend.dto.circulation.*;
import com.library.backend.entity.*;
import com.library.backend.entity.enums.*;
import com.library.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CirculationService {

    public static final int MAX_ACTIVE_LOANS = 5;
    public static final int MAX_RENEWALS = 2;
    public static final BigDecimal DAILY_FINE_RATE = new BigDecimal("0.50");

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final FineRepository fineRepository;
    private final NotificationService notificationService;

    @Transactional
    public TransactionResponse issueBook(IssueRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getUserId()));

        // Rule 1: Max active loans check
        List<Transaction> activeLoans = transactionRepository.findActiveTransactionsByUserId(user.getId());
        if (activeLoans.size() >= MAX_ACTIVE_LOANS) {
            throw new IllegalStateException("User has reached the maximum allowed limit of " + MAX_ACTIVE_LOANS + " active loans.");
        }

        // Rule 2: Find copy
        BookCopy copy = null;
        if (request.getBarcode() != null && !request.getBarcode().isBlank()) {
            copy = bookCopyRepository.findByBarcode(request.getBarcode().trim())
                    .orElseThrow(() -> new IllegalArgumentException("Book copy not found with barcode: " + request.getBarcode()));
        } else if (request.getBookId() != null) {
            List<BookCopy> availableCopies = bookCopyRepository.findByBookIdAndStatus(request.getBookId(), BookCopyStatus.AVAILABLE);
            if (availableCopies.isEmpty()) {
                throw new IllegalStateException("No available copies for book ID: " + request.getBookId());
            }
            copy = availableCopies.get(0);
        } else {
            throw new IllegalArgumentException("Either book ID or barcode must be provided.");
        }

        if (copy.getStatus() != BookCopyStatus.AVAILABLE) {
            throw new IllegalStateException("Book copy " + copy.getBarcode() + " is currently " + copy.getStatus());
        }

        // Rule 3: Update copy & book status
        copy.setStatus(BookCopyStatus.BORROWED);
        bookCopyRepository.save(copy);

        Book book = copy.getBook();
        book.setAvailableCopies(Math.max(0, book.getAvailableCopies() - 1));
        bookRepository.save(book);

        int loanDays = (request.getLoanDays() != null && request.getLoanDays() > 0) ? request.getLoanDays() : 14;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDate = now.plusDays(loanDays);

        Transaction transaction = Transaction.builder()
                .user(user)
                .bookCopy(copy)
                .issueDate(now)
                .dueDate(dueDate)
                .renewalCount(0)
                .status(TransactionStatus.ISSUED)
                .build();

        Transaction savedTx = transactionRepository.save(transaction);

        // Fulfill user reservation if present
        Optional<Reservation> reservationOpt = reservationRepository.findByUserIdAndBookIdAndStatus(
                user.getId(), book.getId(), ReservationStatus.PENDING);
        if (reservationOpt.isPresent()) {
            Reservation res = reservationOpt.get();
            res.setStatus(ReservationStatus.FULFILLED);
            reservationRepository.save(res);
        }

        notificationService.createNotification(
                user,
                "Book Issued",
                "You have checked out '" + book.getTitle() + "' (Barcode: " + copy.getBarcode() + "). Due date is " + dueDate.toLocalDate() + ".",
                NotificationType.INFO
        );

        return mapToTransactionResponse(savedTx);
    }

    @Transactional
    public TransactionResponse returnBook(ReturnRequest request) {
        Transaction transaction = null;
        if (request.getTransactionId() != null) {
            transaction = transactionRepository.findById(request.getTransactionId())
                    .orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + request.getTransactionId()));
        } else if (request.getBarcode() != null) {
            BookCopy copy = bookCopyRepository.findByBarcode(request.getBarcode().trim())
                    .orElseThrow(() -> new IllegalArgumentException("Book copy not found with barcode: " + request.getBarcode()));
            transaction = transactionRepository.findByBookCopyIdAndStatusIn(copy.getId(), List.of(TransactionStatus.ISSUED, TransactionStatus.OVERDUE))
                    .orElseThrow(() -> new IllegalArgumentException("No active transaction found for copy: " + request.getBarcode()));
        } else {
            throw new IllegalArgumentException("Transaction ID or Barcode is required.");
        }

        if (transaction.getStatus() == TransactionStatus.RETURNED) {
            throw new IllegalStateException("Book transaction has already been returned!");
        }

        LocalDateTime now = LocalDateTime.now();
        transaction.setReturnDate(now);
        transaction.setStatus(TransactionStatus.RETURNED);

        BookCopy copy = transaction.getBookCopy();
        BookCopyStatus newCopyStatus = BookCopyStatus.AVAILABLE;
        if (request.getCopyCondition() != null && !request.getCopyCondition().isBlank()) {
            try {
                newCopyStatus = BookCopyStatus.valueOf(request.getCopyCondition().trim().toUpperCase());
            } catch (Exception ignored) {}
        }
        copy.setStatus(newCopyStatus);
        bookCopyRepository.save(copy);

        Book book = copy.getBook();
        if (newCopyStatus == BookCopyStatus.AVAILABLE) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);
        }

        // Auto Fine Calculation if returned past due date
        BigDecimal fineAmount = BigDecimal.ZERO;
        if (now.isAfter(transaction.getDueDate())) {
            long overdueDays = ChronoUnit.DAYS.between(transaction.getDueDate().toLocalDate(), now.toLocalDate());
            if (overdueDays > 0) {
                fineAmount = DAILY_FINE_RATE.multiply(BigDecimal.valueOf(overdueDays));

                Fine fine = Fine.builder()
                        .transaction(transaction)
                        .user(transaction.getUser())
                        .amount(fineAmount)
                        .status(FineStatus.PENDING)
                        .reason("Overdue fine for " + overdueDays + " days late return of '" + book.getTitle() + "'")
                        .createdAt(now)
                        .build();
                fineRepository.save(fine);

                notificationService.createNotification(
                        transaction.getUser(),
                        "Overdue Fine Issued",
                        "Fine of ₹" + fineAmount + " assessed for late return of '" + book.getTitle() + "' (" + overdueDays + " days overdue).",
                        NotificationType.FINE_ISSUED
                );
            }
        }

        // Process Hold Queue / Reservation for next user in line
        List<Reservation> pendingReservations = reservationRepository.findByBookIdAndStatusOrderByQueuePositionAsc(
                book.getId(), ReservationStatus.PENDING);
        if (!pendingReservations.isEmpty() && newCopyStatus == BookCopyStatus.AVAILABLE) {
            Reservation topRes = pendingReservations.get(0);
            notificationService.createNotification(
                    topRes.getUser(),
                    "Reserved Book Available!",
                    "The book '" + book.getTitle() + "' you reserved is now available at the circulation desk.",
                    NotificationType.RESERVATION_AVAILABLE
            );
        }

        Transaction savedTx = transactionRepository.save(transaction);
        return mapToTransactionResponse(savedTx);
    }

    @Transactional
    public TransactionResponse renewBook(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + transactionId));

        if (transaction.getStatus() == TransactionStatus.RETURNED) {
            throw new IllegalStateException("Cannot renew an already returned book.");
        }

        if (transaction.getRenewalCount() >= MAX_RENEWALS) {
            throw new IllegalStateException("Maximum limit of " + MAX_RENEWALS + " renewals reached for this item.");
        }

        // Check if reserved by another user
        Book book = transaction.getBookCopy().getBook();
        List<Reservation> pendingReservations = reservationRepository.findByBookIdAndStatusOrderByQueuePositionAsc(
                book.getId(), ReservationStatus.PENDING);
        if (!pendingReservations.isEmpty()) {
            throw new IllegalStateException("Cannot renew item because another member has reserved this book.");
        }

        transaction.setRenewalCount(transaction.getRenewalCount() + 1);
        transaction.setDueDate(transaction.getDueDate().plusDays(14));
        if (transaction.getStatus() == TransactionStatus.OVERDUE && transaction.getDueDate().isAfter(LocalDateTime.now())) {
            transaction.setStatus(TransactionStatus.ISSUED);
        }

        Transaction savedTx = transactionRepository.save(transaction);

        notificationService.createNotification(
                transaction.getUser(),
                "Loan Renewed",
                "Your loan for '" + book.getTitle() + "' has been extended to " + transaction.getDueDate().toLocalDate() + ".",
                NotificationType.INFO
        );

        return mapToTransactionResponse(savedTx);
    }

    @Transactional
    public ReservationResponse reserveBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        Optional<Reservation> existing = reservationRepository.findByUserIdAndBookIdAndStatus(
                userId, bookId, ReservationStatus.PENDING);
        if (existing.isPresent()) {
            throw new IllegalStateException("You already have an active pending reservation for this book.");
        }

        List<Reservation> currentQueue = reservationRepository.findByBookIdAndStatusOrderByQueuePositionAsc(
                bookId, ReservationStatus.PENDING);
        int queuePosition = currentQueue.size() + 1;

        Reservation reservation = Reservation.builder()
                .user(user)
                .book(book)
                .reservedAt(LocalDateTime.now())
                .status(ReservationStatus.PENDING)
                .queuePosition(queuePosition)
                .build();

        Reservation savedRes = reservationRepository.save(reservation);

        notificationService.createNotification(
                user,
                "Reservation Confirmed",
                "Hold placed for '" + book.getTitle() + "'. Your position in line: #" + queuePosition,
                NotificationType.INFO
        );

        return mapToReservationResponse(savedRes);
    }

    @Transactional
    public void cancelReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + reservationId));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Unauthorized to cancel this reservation.");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    public List<TransactionResponse> getUserTransactions(Long userId) {
        return transactionRepository.findByUserIdOrderByIssueDateDesc(userId).stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> getUserReservations(Long userId) {
        return reservationRepository.findByUserIdOrderByReservedAtDesc(userId).stream()
                .map(this::mapToReservationResponse)
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::mapToReservationResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse mapToTransactionResponse(Transaction tx) {
        BigDecimal fineAmt = BigDecimal.ZERO;
        String fineStat = "NONE";
        Optional<Fine> fineOpt = fineRepository.findByTransactionId(tx.getId());
        if (fineOpt.isPresent()) {
            fineAmt = fineOpt.get().getAmount();
            fineStat = fineOpt.get().getStatus().name();
        } else if (tx.getStatus() == TransactionStatus.ISSUED && LocalDateTime.now().isAfter(tx.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(tx.getDueDate().toLocalDate(), LocalDateTime.now().toLocalDate());
            if (daysLate > 0) {
                fineAmt = DAILY_FINE_RATE.multiply(BigDecimal.valueOf(daysLate));
                fineStat = "ESTIMATED_OVERDUE";
            }
        }

        return TransactionResponse.builder()
                .id(tx.getId())
                .userId(tx.getUser().getId())
                .userName(tx.getUser().getFullName())
                .userEmail(tx.getUser().getEmail())
                .bookId(tx.getBookCopy().getBook().getId())
                .bookTitle(tx.getBookCopy().getBook().getTitle())
                .bookIsbn(tx.getBookCopy().getBook().getIsbn())
                .coverImageUrl(tx.getBookCopy().getBook().getCoverImageUrl())
                .copyId(tx.getBookCopy().getId())
                .barcode(tx.getBookCopy().getBarcode())
                .issueDate(tx.getIssueDate())
                .dueDate(tx.getDueDate())
                .returnDate(tx.getReturnDate())
                .renewalCount(tx.getRenewalCount())
                .status(tx.getStatus().name())
                .fineAmount(fineAmt)
                .fineStatus(fineStat)
                .build();
    }

    private ReservationResponse mapToReservationResponse(Reservation res) {
        return ReservationResponse.builder()
                .id(res.getId())
                .userId(res.getUser().getId())
                .userName(res.getUser().getFullName())
                .userEmail(res.getUser().getEmail())
                .bookId(res.getBook().getId())
                .bookTitle(res.getBook().getTitle())
                .coverImageUrl(res.getBook().getCoverImageUrl())
                .reservedAt(res.getReservedAt())
                .status(res.getStatus().name())
                .queuePosition(res.getQueuePosition())
                .build();
    }
}
