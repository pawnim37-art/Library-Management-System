package com.library.backend.repository;

import com.library.backend.entity.Transaction;
import com.library.backend.entity.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdOrderByIssueDateDesc(Long userId);
    List<Transaction> findByStatus(TransactionStatus status);
    long countByStatus(TransactionStatus status);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.status IN ('ISSUED', 'OVERDUE')")
    List<Transaction> findActiveTransactionsByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.status = 'ISSUED' AND t.dueDate < :now")
    List<Transaction> findOverdueTransactions(@Param("now") LocalDateTime now);

    Optional<Transaction> findByBookCopyIdAndStatusIn(Long bookCopyId, List<TransactionStatus> statuses);
}
