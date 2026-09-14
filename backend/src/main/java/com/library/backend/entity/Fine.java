package com.library.backend.entity;

import com.library.backend.entity.enums.FineStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fines")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FineStatus status = FineStatus.PENDING;

    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public Fine() {}

    public Fine(Long id, Transaction transaction, User user, BigDecimal amount, FineStatus status, String reason, LocalDateTime createdAt, LocalDateTime paidAt) {
        this.id = id;
        this.transaction = transaction;
        this.user = user;
        this.amount = amount;
        this.status = status != null ? status : FineStatus.PENDING;
        this.reason = reason;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public static FineBuilder builder() { return new FineBuilder(); }

    public static class FineBuilder {
        private Long id;
        private Transaction transaction;
        private User user;
        private BigDecimal amount;
        private FineStatus status = FineStatus.PENDING;
        private String reason;
        private LocalDateTime createdAt;
        private LocalDateTime paidAt;

        public FineBuilder id(Long id) { this.id = id; return this; }
        public FineBuilder transaction(Transaction transaction) { this.transaction = transaction; return this; }
        public FineBuilder user(User user) { this.user = user; return this; }
        public FineBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public FineBuilder status(FineStatus status) { this.status = status; return this; }
        public FineBuilder reason(String reason) { this.reason = reason; return this; }
        public FineBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public FineBuilder paidAt(LocalDateTime paidAt) { this.paidAt = paidAt; return this; }

        public Fine build() { return new Fine(id, transaction, user, amount, status, reason, createdAt, paidAt); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public FineStatus getStatus() { return status; }
    public void setStatus(FineStatus status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
}
