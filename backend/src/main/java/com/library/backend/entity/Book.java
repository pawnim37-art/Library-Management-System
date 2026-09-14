package com.library.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "books", indexes = {
        @Index(name = "idx_book_title", columnList = "title"),
        @Index(name = "idx_book_isbn", columnList = "isbn")
})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String isbn;

    private String publisher;

    @Column(name = "publish_year")
    private Integer publishYear;

    @Column(length = 3000)
    private String description;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "total_copies", nullable = false)
    private Integer totalCopies = 1;

    @Column(name = "available_copies", nullable = false)
    private Integer availableCopies = 1;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

    public Book() {}

    public Book(Long id, String title, String isbn, String publisher, Integer publishYear, String description, String coverImageUrl, Author author, Category category, Integer totalCopies, Integer availableCopies, LocalDateTime addedAt) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishYear = publishYear;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
        this.author = author;
        this.category = category;
        this.totalCopies = totalCopies != null ? totalCopies : 1;
        this.availableCopies = availableCopies != null ? availableCopies : 1;
        this.addedAt = addedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (this.addedAt == null) {
            this.addedAt = LocalDateTime.now();
        }
    }

    public static BookBuilder builder() { return new BookBuilder(); }

    public static class BookBuilder {
        private Long id;
        private String title;
        private String isbn;
        private String publisher;
        private Integer publishYear;
        private String description;
        private String coverImageUrl;
        private Author author;
        private Category category;
        private Integer totalCopies = 1;
        private Integer availableCopies = 1;
        private LocalDateTime addedAt;

        public BookBuilder id(Long id) { this.id = id; return this; }
        public BookBuilder title(String title) { this.title = title; return this; }
        public BookBuilder isbn(String isbn) { this.isbn = isbn; return this; }
        public BookBuilder publisher(String publisher) { this.publisher = publisher; return this; }
        public BookBuilder publishYear(Integer publishYear) { this.publishYear = publishYear; return this; }
        public BookBuilder description(String description) { this.description = description; return this; }
        public BookBuilder coverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; return this; }
        public BookBuilder author(Author author) { this.author = author; return this; }
        public BookBuilder category(Category category) { this.category = category; return this; }
        public BookBuilder totalCopies(Integer totalCopies) { this.totalCopies = totalCopies; return this; }
        public BookBuilder availableCopies(Integer availableCopies) { this.availableCopies = availableCopies; return this; }
        public BookBuilder addedAt(LocalDateTime addedAt) { this.addedAt = addedAt; return this; }

        public Book build() {
            return new Book(id, title, isbn, publisher, publishYear, description, coverImageUrl, author, category, totalCopies, availableCopies, addedAt);
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
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public Integer getTotalCopies() { return totalCopies; }
    public void setTotalCopies(Integer totalCopies) { this.totalCopies = totalCopies; }
    public Integer getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(Integer availableCopies) { this.availableCopies = availableCopies; }
    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}
