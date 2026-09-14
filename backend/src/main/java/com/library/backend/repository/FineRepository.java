package com.library.backend.repository;

import com.library.backend.entity.Fine;
import com.library.backend.entity.enums.FineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Fine> findByStatus(FineStatus status);
    Optional<Fine> findByTransactionId(Long transactionId);

    @Query("SELECT SUM(f.amount) FROM Fine f WHERE f.status = :status")
    BigDecimal sumTotalByStatus(@Param("status") FineStatus status);
}
