package com.library.backend.service;

import com.library.backend.dto.dashboard.DashboardStatsDTO;
import com.library.backend.entity.Book;
import com.library.backend.entity.Category;
import com.library.backend.entity.Transaction;
import com.library.backend.entity.enums.BookCopyStatus;
import com.library.backend.entity.enums.FineStatus;
import com.library.backend.entity.enums.ReservationStatus;
import com.library.backend.entity.enums.TransactionStatus;
import com.library.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final ReservationRepository reservationRepository;
    private final FineRepository fineRepository;

    public DashboardStatsDTO getAdminDashboardStats() {
        long totalBooks = bookRepository.count();
        long totalCopies = bookCopyRepository.count();
        long activeMembers = userRepository.countByActiveTrue();
        long issuedBooksCount = bookCopyRepository.countByStatus(BookCopyStatus.BORROWED);
        long overdueBooksCount = transactionRepository.countByStatus(TransactionStatus.OVERDUE);
        long pendingReservationsCount = reservationRepository.countByStatus(ReservationStatus.PENDING);

        BigDecimal finesCollected = fineRepository.sumTotalByStatus(FineStatus.PAID);
        if (finesCollected == null) finesCollected = BigDecimal.ZERO;

        BigDecimal pendingFines = fineRepository.sumTotalByStatus(FineStatus.PENDING);
        if (pendingFines == null) pendingFines = BigDecimal.ZERO;

        // Calculate top borrowed books
        List<Transaction> allTransactions = transactionRepository.findAll();
        Map<Book, Long> bookBorrowMap = new HashMap<>();
        for (Transaction tx : allTransactions) {
            Book b = tx.getBookCopy().getBook();
            bookBorrowMap.put(b, bookBorrowMap.getOrDefault(b, 0L) + 1);
        }

        List<DashboardStatsDTO.TopBookDTO> topBooks = bookBorrowMap.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .map(e -> new DashboardStatsDTO.TopBookDTO(
                        e.getKey().getId(),
                        e.getKey().getTitle(),
                        e.getKey().getAuthor().getName(),
                        e.getKey().getCoverImageUrl(),
                        e.getValue()
                ))
                .collect(Collectors.toList());

        // Calculate category distribution
        List<Book> allBooksList = bookRepository.findAll();
        Map<String, Long> categoryCountMap = new HashMap<>();
        for (Book b : allBooksList) {
            String catName = b.getCategory().getName();
            categoryCountMap.put(catName, categoryCountMap.getOrDefault(catName, 0L) + 1);
        }

        List<DashboardStatsDTO.CategoryDistributionDTO> categoryDist = categoryCountMap.entrySet().stream()
                .map(e -> new DashboardStatsDTO.CategoryDistributionDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        return DashboardStatsDTO.builder()
                .totalBooks(totalBooks)
                .totalCopies(totalCopies)
                .activeMembers(activeMembers)
                .issuedBooksCount(issuedBooksCount)
                .overdueBooksCount(overdueBooksCount)
                .pendingReservationsCount(pendingReservationsCount)
                .totalFinesCollected(finesCollected)
                .pendingFinesAmount(pendingFines)
                .topBorrowedBooks(topBooks)
                .categoryDistribution(categoryDist)
                .build();
    }
}
