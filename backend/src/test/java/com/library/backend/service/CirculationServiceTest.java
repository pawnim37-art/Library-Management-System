package com.library.backend.service;

import com.library.backend.dto.circulation.IssueRequest;
import com.library.backend.dto.circulation.ReturnRequest;
import com.library.backend.dto.circulation.TransactionResponse;
import com.library.backend.entity.*;
import com.library.backend.entity.enums.*;
import com.library.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CirculationServiceTest {

    @Mock private TransactionRepository transactionRepository;
    @Mock private BookRepository bookRepository;
    @Mock private BookCopyRepository bookCopyRepository;
    @Mock private UserRepository userRepository;
    @Mock private ReservationRepository reservationRepository;
    @Mock private FineRepository fineRepository;
    @Mock private NotificationService notificationService;

    @InjectMocks
    private CirculationService circulationService;

    private User sampleUser;
    private Book sampleBook;
    private BookCopy sampleCopy;
    private Transaction activeTransaction;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder().id(1L).email("user@library.com").fullName("Test User").role(Role.ROLE_MEMBER).build();
        sampleBook = Book.builder().id(10L).title("Test Book").isbn("1234567890").availableCopies(2).totalCopies(2).build();
        sampleCopy = BookCopy.builder().id(100L).book(sampleBook).barcode("BC-100-1").status(BookCopyStatus.AVAILABLE).build();

        activeTransaction = Transaction.builder()
                .id(500L)
                .user(sampleUser)
                .bookCopy(sampleCopy)
                .issueDate(LocalDateTime.now().minusDays(10))
                .dueDate(LocalDateTime.now().plusDays(4))
                .renewalCount(0)
                .status(TransactionStatus.ISSUED)
                .build();
    }

    @Test
    @DisplayName("Should throw exception when user exceeds maximum active loan limit of 5")
    void testIssueBookExceedsMaxLoanLimit() {
        List<Transaction> activeLoans = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            activeLoans.add(Transaction.builder().id((long) i).build());
        }

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(transactionRepository.findActiveTransactionsByUserId(1L)).thenReturn(activeLoans);

        IssueRequest request = new IssueRequest();
        request.setUserId(1L);
        request.setBookId(10L);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            circulationService.issueBook(request);
        });

        assertTrue(exception.getMessage().contains("maximum allowed limit of 5 active loans"));
    }

    @Test
    @DisplayName("Should calculate fine of $0.50/day when book is returned past due date")
    void testReturnBookWithOverdueFineCalculation() {
        LocalDateTime issueDate = LocalDateTime.now().minusDays(20);
        LocalDateTime dueDate = LocalDateTime.now().minusDays(4); // 4 days overdue

        Transaction overdueTx = Transaction.builder()
                .id(501L)
                .user(sampleUser)
                .bookCopy(sampleCopy)
                .issueDate(issueDate)
                .dueDate(dueDate)
                .status(TransactionStatus.ISSUED)
                .build();

        when(transactionRepository.findById(501L)).thenReturn(Optional.of(overdueTx));
        when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(sampleCopy);
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setTransactionId(501L);

        TransactionResponse response = circulationService.returnBook(returnRequest);

        assertNotNull(response);
        assertEquals("RETURNED", response.getStatus());

        // Verify Fine entity creation of 4 days * $0.50 = $2.00
        verify(fineRepository, times(1)).save(argThat(fine ->
                fine.getAmount().compareTo(new BigDecimal("2.00")) == 0 &&
                fine.getUser().getId().equals(1L)
        ));
    }

    @Test
    @DisplayName("Should throw exception when attempting a third renewal (max 2 allowed)")
    void testRenewBookMaxLimitReached() {
        Transaction maxRenewedTx = Transaction.builder()
                .id(502L)
                .user(sampleUser)
                .bookCopy(sampleCopy)
                .issueDate(LocalDateTime.now().minusDays(30))
                .dueDate(LocalDateTime.now().plusDays(5))
                .renewalCount(2) // Already renewed twice!
                .status(TransactionStatus.ISSUED)
                .build();

        when(transactionRepository.findById(502L)).thenReturn(Optional.of(maxRenewedTx));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            circulationService.renewBook(502L);
        });

        assertTrue(exception.getMessage().contains("Maximum limit of 2 renewals reached"));
    }
}
