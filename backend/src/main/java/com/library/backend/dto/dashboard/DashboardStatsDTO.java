package com.library.backend.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

public class DashboardStatsDTO {
    private long totalBooks;
    private long totalCopies;
    private long activeMembers;
    private long issuedBooksCount;
    private long overdueBooksCount;
    private long pendingReservationsCount;
    private BigDecimal totalFinesCollected;
    private BigDecimal pendingFinesAmount;
    private List<TopBookDTO> topBorrowedBooks;
    private List<CategoryDistributionDTO> categoryDistribution;

    public DashboardStatsDTO() {}

    public DashboardStatsDTO(long totalBooks, long totalCopies, long activeMembers, long issuedBooksCount, long overdueBooksCount, long pendingReservationsCount, BigDecimal totalFinesCollected, BigDecimal pendingFinesAmount, List<TopBookDTO> topBorrowedBooks, List<CategoryDistributionDTO> categoryDistribution) {
        this.totalBooks = totalBooks;
        this.totalCopies = totalCopies;
        this.activeMembers = activeMembers;
        this.issuedBooksCount = issuedBooksCount;
        this.overdueBooksCount = overdueBooksCount;
        this.pendingReservationsCount = pendingReservationsCount;
        this.totalFinesCollected = totalFinesCollected;
        this.pendingFinesAmount = pendingFinesAmount;
        this.topBorrowedBooks = topBorrowedBooks;
        this.categoryDistribution = categoryDistribution;
    }

    public static DashboardStatsDTOBuilder builder() { return new DashboardStatsDTOBuilder(); }

    public static class DashboardStatsDTOBuilder {
        private long totalBooks;
        private long totalCopies;
        private long activeMembers;
        private long issuedBooksCount;
        private long overdueBooksCount;
        private long pendingReservationsCount;
        private BigDecimal totalFinesCollected;
        private BigDecimal pendingFinesAmount;
        private List<TopBookDTO> topBorrowedBooks;
        private List<CategoryDistributionDTO> categoryDistribution;

        public DashboardStatsDTOBuilder totalBooks(long totalBooks) { this.totalBooks = totalBooks; return this; }
        public DashboardStatsDTOBuilder totalCopies(long totalCopies) { this.totalCopies = totalCopies; return this; }
        public DashboardStatsDTOBuilder activeMembers(long activeMembers) { this.activeMembers = activeMembers; return this; }
        public DashboardStatsDTOBuilder issuedBooksCount(long issuedBooksCount) { this.issuedBooksCount = issuedBooksCount; return this; }
        public DashboardStatsDTOBuilder overdueBooksCount(long overdueBooksCount) { this.overdueBooksCount = overdueBooksCount; return this; }
        public DashboardStatsDTOBuilder pendingReservationsCount(long pendingReservationsCount) { this.pendingReservationsCount = pendingReservationsCount; return this; }
        public DashboardStatsDTOBuilder totalFinesCollected(BigDecimal totalFinesCollected) { this.totalFinesCollected = totalFinesCollected; return this; }
        public DashboardStatsDTOBuilder pendingFinesAmount(BigDecimal pendingFinesAmount) { this.pendingFinesAmount = pendingFinesAmount; return this; }
        public DashboardStatsDTOBuilder topBorrowedBooks(List<TopBookDTO> topBorrowedBooks) { this.topBorrowedBooks = topBorrowedBooks; return this; }
        public DashboardStatsDTOBuilder categoryDistribution(List<CategoryDistributionDTO> categoryDistribution) { this.categoryDistribution = categoryDistribution; return this; }

        public DashboardStatsDTO build() {
            return new DashboardStatsDTO(totalBooks, totalCopies, activeMembers, issuedBooksCount, overdueBooksCount, pendingReservationsCount, totalFinesCollected, pendingFinesAmount, topBorrowedBooks, categoryDistribution);
        }
    }

    public static class TopBookDTO {
        private Long bookId;
        private String title;
        private String authorName;
        private String coverImageUrl;
        private long borrowCount;

        public TopBookDTO() {}

        public TopBookDTO(Long bookId, String title, String authorName, String coverImageUrl, long borrowCount) {
            this.bookId = bookId;
            this.title = title;
            this.authorName = authorName;
            this.coverImageUrl = coverImageUrl;
            this.borrowCount = borrowCount;
        }

        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getAuthorName() { return authorName; }
        public void setAuthorName(String authorName) { this.authorName = authorName; }
        public String getCoverImageUrl() { return coverImageUrl; }
        public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
        public long getBorrowCount() { return borrowCount; }
        public void setBorrowCount(long borrowCount) { this.borrowCount = borrowCount; }
    }

    public static class CategoryDistributionDTO {
        private String categoryName;
        private long bookCount;

        public CategoryDistributionDTO() {}

        public CategoryDistributionDTO(String categoryName, long bookCount) {
            this.categoryName = categoryName;
            this.bookCount = bookCount;
        }

        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
        public long getBookCount() { return bookCount; }
        public void setBookCount(long bookCount) { this.bookCount = bookCount; }
    }

    public long getTotalBooks() { return totalBooks; }
    public void setTotalBooks(long totalBooks) { this.totalBooks = totalBooks; }
    public long getTotalCopies() { return totalCopies; }
    public void setTotalCopies(long totalCopies) { this.totalCopies = totalCopies; }
    public long getActiveMembers() { return activeMembers; }
    public void setActiveMembers(long activeMembers) { this.activeMembers = activeMembers; }
    public long getIssuedBooksCount() { return issuedBooksCount; }
    public void setIssuedBooksCount(long issuedBooksCount) { this.issuedBooksCount = issuedBooksCount; }
    public long getOverdueBooksCount() { return overdueBooksCount; }
    public void setOverdueBooksCount(long overdueBooksCount) { this.overdueBooksCount = overdueBooksCount; }
    public long getPendingReservationsCount() { return pendingReservationsCount; }
    public void setPendingReservationsCount(long pendingReservationsCount) { this.pendingReservationsCount = pendingReservationsCount; }
    public BigDecimal getTotalFinesCollected() { return totalFinesCollected; }
    public void setTotalFinesCollected(BigDecimal totalFinesCollected) { this.totalFinesCollected = totalFinesCollected; }
    public BigDecimal getPendingFinesAmount() { return pendingFinesAmount; }
    public void setPendingFinesAmount(BigDecimal pendingFinesAmount) { this.pendingFinesAmount = pendingFinesAmount; }
    public List<TopBookDTO> getTopBorrowedBooks() { return topBorrowedBooks; }
    public void setTopBorrowedBooks(List<TopBookDTO> topBorrowedBooks) { this.topBorrowedBooks = topBorrowedBooks; }
    public List<CategoryDistributionDTO> getCategoryDistribution() { return categoryDistribution; }
    public void setCategoryDistribution(List<CategoryDistributionDTO> categoryDistribution) { this.categoryDistribution = categoryDistribution; }
}
