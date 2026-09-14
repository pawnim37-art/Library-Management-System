package com.library.backend.service;

import com.library.backend.dto.book.*;
import com.library.backend.entity.Author;
import com.library.backend.entity.Book;
import com.library.backend.entity.BookCopy;
import com.library.backend.entity.Category;
import com.library.backend.entity.enums.BookCopyStatus;
import com.library.backend.repository.AuthorRepository;
import com.library.backend.repository.BookCopyRepository;
import com.library.backend.repository.BookRepository;
import com.library.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public List<BookResponse> getAllBooks(String query, Long categoryId, Boolean onlyAvailable) {
        boolean filterAvailable = Boolean.TRUE.equals(onlyAvailable);
        String searchQuery = (query != null && !query.trim().isEmpty()) ? query.trim() : null;

        List<Book> books = bookRepository.searchBooks(searchQuery, categoryId, filterAvailable);
        return books.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + id));
        return mapToResponse(book);
    }

    @Transactional
    public BookResponse createBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + request.getIsbn() + " already exists!");
        }

        Author author = authorRepository.findByNameIgnoreCase(request.getAuthorName().trim())
                .orElseGet(() -> authorRepository.save(Author.builder()
                        .name(request.getAuthorName().trim())
                        .biography(request.getAuthorBio())
                        .build()));

        Category category = categoryRepository.findByNameIgnoreCase(request.getCategoryName().trim())
                .orElseGet(() -> categoryRepository.save(Category.builder()
                        .name(request.getCategoryName().trim())
                        .department(request.getDepartment() != null ? request.getDepartment().trim() : "General")
                        .description("Category for " + request.getCategoryName().trim())
                        .build()));

        int totalCopies = request.getTotalCopies() != null ? request.getTotalCopies() : 1;

        Book book = Book.builder()
                .title(request.getTitle().trim())
                .isbn(request.getIsbn().trim())
                .author(author)
                .category(category)
                .publisher(request.getPublisher())
                .publishYear(request.getPublishYear())
                .description(request.getDescription())
                .coverImageUrl(request.getCoverImageUrl())
                .totalCopies(totalCopies)
                .availableCopies(totalCopies)
                .build();

        Book savedBook = bookRepository.save(book);

        // Generate physical copies
        for (int i = 1; i <= totalCopies; i++) {
            String barcode = "BC-" + savedBook.getId() + "-" + i;
            BookCopy copy = BookCopy.builder()
                    .book(savedBook)
                    .barcode(barcode)
                    .status(BookCopyStatus.AVAILABLE)
                    .conditionNote("New")
                    .build();
            bookCopyRepository.save(copy);
        }

        return mapToResponse(savedBook);
    }

    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + id));

        if (!book.getIsbn().equals(request.getIsbn()) && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + request.getIsbn() + " already exists!");
        }

        Author author = authorRepository.findByNameIgnoreCase(request.getAuthorName().trim())
                .orElseGet(() -> authorRepository.save(Author.builder()
                        .name(request.getAuthorName().trim())
                        .biography(request.getAuthorBio())
                        .build()));

        Category category = categoryRepository.findByNameIgnoreCase(request.getCategoryName().trim())
                .orElseGet(() -> categoryRepository.save(Category.builder()
                        .name(request.getCategoryName().trim())
                        .department(request.getDepartment() != null ? request.getDepartment().trim() : "General")
                        .description("Category for " + request.getCategoryName().trim())
                        .build()));

        book.setTitle(request.getTitle().trim());
        book.setIsbn(request.getIsbn().trim());
        book.setAuthor(author);
        book.setCategory(category);
        book.setPublisher(request.getPublisher());
        book.setPublishYear(request.getPublishYear());
        book.setDescription(request.getDescription());
        if (request.getCoverImageUrl() != null && !request.getCoverImageUrl().isBlank()) {
            book.setCoverImageUrl(request.getCoverImageUrl());
        }

        // Adjust total copies if increased
        if (request.getTotalCopies() != null && request.getTotalCopies() > book.getTotalCopies()) {
            int addedCount = request.getTotalCopies() - book.getTotalCopies();
            for (int i = 1; i <= addedCount; i++) {
                int nextIndex = book.getTotalCopies() + i;
                BookCopy copy = BookCopy.builder()
                        .book(book)
                        .barcode("BC-" + book.getId() + "-" + nextIndex)
                        .status(BookCopyStatus.AVAILABLE)
                        .conditionNote("Added copy")
                        .build();
                bookCopyRepository.save(copy);
            }
            book.setAvailableCopies(book.getAvailableCopies() + addedCount);
            book.setTotalCopies(request.getTotalCopies());
        }

        Book updatedBook = bookRepository.save(book);
        return mapToResponse(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + id));

        List<BookCopy> copies = bookCopyRepository.findByBookId(id);
        boolean checkedOut = copies.stream().anyMatch(c -> c.getStatus() == BookCopyStatus.BORROWED);
        if (checkedOut) {
            throw new IllegalStateException("Cannot delete book while copies are currently checked out!");
        }

        bookCopyRepository.deleteAll(copies);
        bookRepository.delete(book);
    }

    public String uploadCoverImage(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to store image file: " + e.getMessage());
        }
    }

    @Transactional
    public int bulkImportCSV(MultipartFile file) {
        int importedCount = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord record : csvParser) {
                String title = record.get("title");
                String isbn = record.get("isbn");
                String authorName = record.get("author");
                String categoryName = record.get("category");
                String publisher = record.isMapped("publisher") ? record.get("publisher") : "Standard Pub";
                String yearStr = record.isMapped("year") ? record.get("year") : "2024";
                String copiesStr = record.isMapped("copies") ? record.get("copies") : "2";

                if (title == null || isbn == null || authorName == null || categoryName == null) {
                    continue; // Skip invalid row
                }

                if (bookRepository.existsByIsbn(isbn)) {
                    continue; // Skip existing ISBN
                }

                BookRequest request = new BookRequest();
                request.setTitle(title);
                request.setIsbn(isbn);
                request.setAuthorName(authorName);
                request.setCategoryName(categoryName);
                request.setPublisher(publisher);
                try {
                    request.setPublishYear(Integer.parseInt(yearStr));
                } catch (NumberFormatException e) {
                    request.setPublishYear(2024);
                }
                try {
                    request.setTotalCopies(Integer.parseInt(copiesStr));
                } catch (NumberFormatException e) {
                    request.setTotalCopies(2);
                }
                request.setDescription("Bulk imported catalog book.");

                createBook(request);
                importedCount++;
            }
        } catch (Exception e) {
            throw new RuntimeException("CSV import failed: " + e.getMessage());
        }
        return importedCount;
    }

    private BookResponse mapToResponse(Book book) {
        List<BookCopyDTO> copyDTOs = bookCopyRepository.findByBookId(book.getId()).stream()
                .map(c -> BookCopyDTO.builder()
                        .id(c.getId())
                        .bookId(book.getId())
                        .barcode(c.getBarcode())
                        .status(c.getStatus().name())
                        .conditionNote(c.getConditionNote())
                        .build())
                .collect(Collectors.toList());

        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .publishYear(book.getPublishYear())
                .description(book.getDescription())
                .coverImageUrl(book.getCoverImageUrl())
                .authorId(book.getAuthor().getId())
                .authorName(book.getAuthor().getName())
                .authorBio(book.getAuthor().getBiography())
                .categoryId(book.getCategory().getId())
                .categoryName(book.getCategory().getName())
                .department(book.getCategory().getDepartment())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .addedAt(book.getAddedAt())
                .copies(copyDTOs)
                .build();
    }
}
