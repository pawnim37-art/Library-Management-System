package com.library.backend.dto.circulation;

import java.time.LocalDateTime;

public class ReservationResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long bookId;
    private String bookTitle;
    private String coverImageUrl;
    private LocalDateTime reservedAt;
    private String status;
    private Integer queuePosition;

    public ReservationResponse() {}

    public ReservationResponse(Long id, Long userId, String userName, String userEmail, Long bookId, String bookTitle, String coverImageUrl, LocalDateTime reservedAt, String status, Integer queuePosition) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.coverImageUrl = coverImageUrl;
        this.reservedAt = reservedAt;
        this.status = status;
        this.queuePosition = queuePosition;
    }

    public static ReservationResponseBuilder builder() { return new ReservationResponseBuilder(); }

    public static class ReservationResponseBuilder {
        private Long id;
        private Long userId;
        private String userName;
        private String userEmail;
        private Long bookId;
        private String bookTitle;
        private String coverImageUrl;
        private LocalDateTime reservedAt;
        private String status;
        private Integer queuePosition;

        public ReservationResponseBuilder id(Long id) { this.id = id; return this; }
        public ReservationResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public ReservationResponseBuilder userName(String userName) { this.userName = userName; return this; }
        public ReservationResponseBuilder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public ReservationResponseBuilder bookId(Long bookId) { this.bookId = bookId; return this; }
        public ReservationResponseBuilder bookTitle(String bookTitle) { this.bookTitle = bookTitle; return this; }
        public ReservationResponseBuilder coverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; return this; }
        public ReservationResponseBuilder reservedAt(LocalDateTime reservedAt) { this.reservedAt = reservedAt; return this; }
        public ReservationResponseBuilder status(String status) { this.status = status; return this; }
        public ReservationResponseBuilder queuePosition(Integer queuePosition) { this.queuePosition = queuePosition; return this; }

        public ReservationResponse build() {
            return new ReservationResponse(id, userId, userName, userEmail, bookId, bookTitle, coverImageUrl, reservedAt, status, queuePosition);
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
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public LocalDateTime getReservedAt() { return reservedAt; }
    public void setReservedAt(LocalDateTime reservedAt) { this.reservedAt = reservedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getQueuePosition() { return queuePosition; }
    public void setQueuePosition(Integer queuePosition) { this.queuePosition = queuePosition; }
}
