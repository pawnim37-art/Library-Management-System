package com.library.backend.dto.book;

public class BookCopyDTO {
    private Long id;
    private Long bookId;
    private String barcode;
    private String status;
    private String conditionNote;

    public BookCopyDTO() {}

    public BookCopyDTO(Long id, Long bookId, String barcode, String status, String conditionNote) {
        this.id = id;
        this.bookId = bookId;
        this.barcode = barcode;
        this.status = status;
        this.conditionNote = conditionNote;
    }

    public static BookCopyDTOBuilder builder() { return new BookCopyDTOBuilder(); }

    public static class BookCopyDTOBuilder {
        private Long id;
        private Long bookId;
        private String barcode;
        private String status;
        private String conditionNote;

        public BookCopyDTOBuilder id(Long id) { this.id = id; return this; }
        public BookCopyDTOBuilder bookId(Long bookId) { this.bookId = bookId; return this; }
        public BookCopyDTOBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public BookCopyDTOBuilder status(String status) { this.status = status; return this; }
        public BookCopyDTOBuilder conditionNote(String conditionNote) { this.conditionNote = conditionNote; return this; }

        public BookCopyDTO build() { return new BookCopyDTO(id, bookId, barcode, status, conditionNote); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getConditionNote() { return conditionNote; }
    public void setConditionNote(String conditionNote) { this.conditionNote = conditionNote; }
}
