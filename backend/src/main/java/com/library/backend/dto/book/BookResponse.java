package com.library.backend.dto.book;

import java.time.LocalDateTime;
import java.util.List;

public class BookResponse {
    private Long id;
    private String title;
    private String isbn;
    private String publisher;
    private Integer publishYear;
    private String description;
    private String coverImageUrl;
    private Long authorId;
    private String authorName;
    private String authorBio;
    private Long categoryId;
    private String categoryName;
    private String department;
    private Integer totalCopies;
    private Integer availableCopies;
    private LocalDateTime addedAt;
    private List<BookCopyDTO> copies;

    public BookResponse() {}

    public BookResponse(Long id, String title, String isbn, String publisher, Integer publishYear, String description, String coverImageUrl, Long authorId, String authorName, String authorBio, Long categoryId, String categoryName, String department, Integer totalCopies, Integer availableCopies, LocalDateTime addedAt, List<BookCopyDTO> copies) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishYear = publishYear;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorBio = authorBio;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.department = department;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.addedAt = addedAt;
        this.copies = copies;
    }

    public static BookResponseBuilder builder() { return new BookResponseBuilder(); }

    public static class BookResponseBuilder {
        private Long id;
        private String title;
        private String isbn;
        private String publisher;
        private Integer publishYear;
        private String description;
        private String coverImageUrl;
        private Long authorId;
        private String authorName;
        private String authorBio;
        private Long categoryId;
        private String categoryName;
        private String department;
        private Integer totalCopies;
        private Integer availableCopies;
        private LocalDateTime addedAt;
        private List<BookCopyDTO> copies;

        public BookResponseBuilder id(Long id) { this.id = id; return this; }
        public BookResponseBuilder title(String title) { this.title = title; return this; }
        public BookResponseBuilder isbn(String isbn) { this.isbn = isbn; return this; }
        public BookResponseBuilder publisher(String publisher) { this.publisher = publisher; return this; }
        public BookResponseBuilder publishYear(Integer publishYear) { this.publishYear = publishYear; return this; }
        public BookResponseBuilder description(String description) { this.description = description; return this; }
        public BookResponseBuilder coverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; return this; }
        public BookResponseBuilder authorId(Long authorId) { this.authorId = authorId; return this; }
        public BookResponseBuilder authorName(String authorName) { this.authorName = authorName; return this; }
        public BookResponseBuilder authorBio(String authorBio) { this.authorBio = authorBio; return this; }
        public BookResponseBuilder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
        public BookResponseBuilder categoryName(String categoryName) { this.categoryName = categoryName; return this; }
        public BookResponseBuilder department(String department) { this.department = department; return this; }
        public BookResponseBuilder totalCopies(Integer totalCopies) { this.totalCopies = totalCopies; return this; }
        public BookResponseBuilder availableCopies(Integer availableCopies) { this.availableCopies = availableCopies; return this; }
        public BookResponseBuilder addedAt(LocalDateTime addedAt) { this.addedAt = addedAt; return this; }
        public BookResponseBuilder copies(List<BookCopyDTO> copies) { this.copies = copies; return this; }

        public BookResponse build() {
            return new BookResponse(id, title, isbn, publisher, publishYear, description, coverImageUrl, authorId, authorName, authorBio, categoryId, categoryName, department, totalCopies, availableCopies, addedAt, copies);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public Integer getPublishYear() { return publishYear; }
    public void setPublishYear(Integer publishYear) { this.publishYear = publishYear; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getAuthorBio() { return authorBio; }
    public void setAuthorBio(String authorBio) { this.authorBio = authorBio; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Integer getTotalCopies() { return totalCopies; }
    public void setTotalCopies(Integer totalCopies) { this.totalCopies = totalCopies; }
    public Integer getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(Integer availableCopies) { this.availableCopies = availableCopies; }
    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
    public List<BookCopyDTO> getCopies() { return copies; }
    public void setCopies(List<BookCopyDTO> copies) { this.copies = copies; }
}
