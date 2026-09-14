package com.library.backend.dto.circulation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long bookId;
    private String bookTitle;
    private String bookIsbn;
    private String coverImageUrl;
    private Long copyId;
    private String barcode;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private Integer renewalCount;
    private String status;
    private BigDecimal fineAmount;
    private String fineStatus;

    public TransactionResponse() {}

    public TransactionResponse(Long id, Long userId, String userName, String userEmail, Long bookId, String bookTitle, String bookIsbn, String coverImageUrl, Long copyId, String barcode, LocalDateTime issueDate, LocalDateTime dueDate, LocalDateTime returnDate, Integer renewalCount, String status, BigDecimal fineAmount, String fineStatus) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookIsbn = bookIsbn;
        this.coverImageUrl = coverImageUrl;
        this.copyId = copyId;
        this.barcode = barcode;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.renewalCount = renewalCount;
        this.status = status;
        this.fineAmount = fineAmount;
        this.fineStatus = fineStatus;
    }

    public static TransactionResponseBuilder builder() { return new TransactionResponseBuilder(); }

    public static class TransactionResponseBuilder {
        private Long id;
        private Long userId;
        private String userName;
        private String userEmail;
        private Long bookId;
        private String bookTitle;
        private String bookIsbn;
        private String coverImageUrl;
        private Long copyId;
        private String barcode;
        private LocalDateTime issueDate;
        private LocalDateTime dueDate;
        private LocalDateTime returnDate;
        private Integer renewalCount;
        private String status;
        private BigDecimal fineAmount;
        private String fineStatus;

        public TransactionResponseBuilder id(Long id) { this.id = id; return this; }
        public TransactionResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public TransactionResponseBuilder userName(String userName) { this.userName = userName; return this; }
        public TransactionResponseBuilder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public TransactionResponseBuilder bookId(Long bookId) { this.bookId = bookId; return this; }
        public TransactionResponseBuilder bookTitle(String bookTitle) { this.bookTitle = bookTitle; return this; }
        public TransactionResponseBuilder bookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; return this; }
        public TransactionResponseBuilder coverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; return this; }
        public TransactionResponseBuilder copyId(Long copyId) { this.copyId = copyId; return this; }
        public TransactionResponseBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public TransactionResponseBuilder issueDate(LocalDateTime issueDate) { this.issueDate = issueDate; return this; }
        public TransactionResponseBuilder dueDate(LocalDateTime dueDate) { this.dueDate = dueDate; return this; }
        public TransactionResponseBuilder returnDate(LocalDateTime returnDate) { this.returnDate = returnDate; return this; }
        public TransactionResponseBuilder renewalCount(Integer renewalCount) { this.renewalCount = renewalCount; return this; }
        public TransactionResponseBuilder status(String status) { this.status = status; return this; }
        public TransactionResponseBuilder fineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; return this; }
        public TransactionResponseBuilder fineStatus(String fineStatus) { this.fineStatus = fineStatus; return this; }

        public TransactionResponse build() {
            return new TransactionResponse(id, userId, userName, userEmail, bookId, bookTitle, bookIsbn, coverImageUrl, copyId, barcode, issueDate, dueDate, returnDate, renewalCount, status, fineAmount, fineStatus);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public String getBookIsbn() { return bookIsbn; }
    public void setBookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public Long getCopyId() { return copyId; }
    public void setCopyId(Long copyId) { this.copyId = copyId; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public LocalDateTime getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDateTime issueDate) { this.issueDate = issueDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
    public Integer getRenewalCount() { return renewalCount; }
    public void setRenewalCount(Integer renewalCount) { this.renewalCount = renewalCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }
    public String getFineStatus() { return fineStatus; }
    public void setFineStatus(String fineStatus) { this.fineStatus = fineStatus; }
}
