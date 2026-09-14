package com.library.backend.dto.circulation;

import jakarta.validation.constraints.NotNull;

public class IssueRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    private Long bookId;
    private String barcode;
    private Integer loanDays;

    public IssueRequest() {}

    public IssueRequest(Long userId, Long bookId, String barcode, Integer loanDays) {
        this.userId = userId;
        this.bookId = bookId;
        this.barcode = barcode;
        this.loanDays = loanDays;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public Integer getLoanDays() { return loanDays; }
    public void setLoanDays(Integer loanDays) { this.loanDays = loanDays; }
}
