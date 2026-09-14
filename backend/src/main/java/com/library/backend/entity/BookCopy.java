package com.library.backend.entity;

import com.library.backend.entity.enums.BookCopyStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "book_copies")
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false, unique = true)
    private String barcode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookCopyStatus status = BookCopyStatus.AVAILABLE;

    @Column(name = "condition_note")
    private String conditionNote;

    public BookCopy() {}

    public BookCopy(Long id, Book book, String barcode, BookCopyStatus status, String conditionNote) {
        this.id = id;
        this.book = book;
        this.barcode = barcode;
        this.status = status != null ? status : BookCopyStatus.AVAILABLE;
        this.conditionNote = conditionNote;
    }

    public static BookCopyBuilder builder() { return new BookCopyBuilder(); }

    public static class BookCopyBuilder {
        private Long id;
        private Book book;
        private String barcode;
        private BookCopyStatus status = BookCopyStatus.AVAILABLE;
        private String conditionNote;

        public BookCopyBuilder id(Long id) { this.id = id; return this; }
        public BookCopyBuilder book(Book book) { this.book = book; return this; }
        public BookCopyBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public BookCopyBuilder status(BookCopyStatus status) { this.status = status; return this; }
        public BookCopyBuilder conditionNote(String conditionNote) { this.conditionNote = conditionNote; return this; }

        public BookCopy build() { return new BookCopy(id, book, barcode, status, conditionNote); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public BookCopyStatus getStatus() { return status; }
    public void setStatus(BookCopyStatus status) { this.status = status; }
    public String getConditionNote() { return conditionNote; }
    public void setConditionNote(String conditionNote) { this.conditionNote = conditionNote; }
}
