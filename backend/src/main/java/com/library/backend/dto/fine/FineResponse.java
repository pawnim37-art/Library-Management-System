package com.library.backend.dto.fine;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FineResponse {
    private Long id;
    private Long transactionId;
    private Long userId;
    private String userName;
    private String userEmail;
    private String bookTitle;
    private BigDecimal amount;
    private String status;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    public FineResponse() {}

    public FineResponse(Long id, Long transactionId, Long userId, String userName, String userEmail, String bookTitle, BigDecimal amount, String status, String reason, LocalDateTime createdAt, LocalDateTime paidAt) {
        this.id = id;
        this.transactionId = transactionId;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.bookTitle = bookTitle;
        this.amount = amount;
        this.status = status;
        this.reason = reason;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
    }

    public static FineResponseBuilder builder() { return new FineResponseBuilder(); }

    public static class FineResponseBuilder {
        private Long id;
        private Long transactionId;
        private Long userId;
        private String userName;
        private String userEmail;
        private String bookTitle;
        private BigDecimal amount;
        private String status;
        private String reason;
        private LocalDateTime createdAt;
        private LocalDateTime paidAt;

        public FineResponseBuilder id(Long id) { this.id = id; return this; }
        public FineResponseBuilder transactionId(Long transactionId) { this.transactionId = transactionId; return this; }
        public FineResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public FineResponseBuilder userName(String userName) { this.userName = userName; return this; }
        public FineResponseBuilder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public FineResponseBuilder bookTitle(String bookTitle) { this.bookTitle = bookTitle; return this; }
        public FineResponseBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public FineResponseBuilder status(String status) { this.status = status; return this; }
        public FineResponseBuilder reason(String reason) { this.reason = reason; return this; }
        public FineResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public FineResponseBuilder paidAt(LocalDateTime paidAt) { this.paidAt = paidAt; return this; }

        public FineResponse build() {
            return new FineResponse(id, transactionId, userId, userName, userEmail, bookTitle, amount, status, reason, createdAt, paidAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
}
