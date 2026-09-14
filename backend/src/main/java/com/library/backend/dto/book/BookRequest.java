package com.library.backend.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotBlank(message = "Author name is required")
    private String authorName;

    private String authorBio;

    @NotBlank(message = "Category name is required")
    private String categoryName;

    private String department;
    private String publisher;
    private Integer publishYear;
    private String description;
    private String coverImageUrl;

    @NotNull(message = "Total copies count is required")
    @Min(value = 1, message = "Total copies must be at least 1")
    private Integer totalCopies;

    public BookRequest() {}

    public BookRequest(String title, String isbn, String authorName, String authorBio, String categoryName, String department, String publisher, Integer publishYear, String description, String coverImageUrl, Integer totalCopies) {
        this.title = title;
        this.isbn = isbn;
        this.authorName = authorName;
        this.authorBio = authorBio;
        this.categoryName = categoryName;
        this.department = department;
        this.publisher = publisher;
        this.publishYear = publishYear;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.totalCopies = totalCopies;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public String getAuthorBio() { return authorBio; }
    public void setAuthorBio(String authorBio) { this.authorBio = authorBio; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public Integer getPublishYear() { return publishYear; }
    public void setPublishYear(Integer publishYear) { this.publishYear = publishYear; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public Integer getTotalCopies() { return totalCopies; }
    public void setTotalCopies(Integer totalCopies) { this.totalCopies = totalCopies; }
}
