package com.library.backend.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.library.backend.entity.Book;
import com.library.backend.entity.Fine;
import com.library.backend.entity.Transaction;
import com.library.backend.entity.enums.FineStatus;
import com.library.backend.entity.enums.TransactionStatus;
import com.library.backend.repository.BookRepository;
import com.library.backend.repository.FineRepository;
import com.library.backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final FineRepository fineRepository;

    public ByteArrayInputStream generateCirculationPdfReport() {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
            Paragraph title = new Paragraph("Library Management System - Circulation Report", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            Paragraph date = new Paragraph("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            date.setAlignment(Paragraph.ALIGN_CENTER);
            date.setSpacingAfter(20);
            document.add(date);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 3, 3, 2, 2, 2});

            String[] headers = {"Tx ID", "Member", "Book Title", "Issue Date", "Due Date", "Status"};
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(new Color(79, 70, 229)); // Indigo
                cell.setPadding(6);
                table.addCell(cell);
            }

            List<Transaction> transactions = transactionRepository.findAll();
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            for (Transaction tx : transactions) {
                table.addCell(new Phrase(String.valueOf(tx.getId()), cellFont));
                table.addCell(new Phrase(tx.getUser().getFullName(), cellFont));
                table.addCell(new Phrase(tx.getBookCopy().getBook().getTitle(), cellFont));
                table.addCell(new Phrase(tx.getIssueDate().toLocalDate().toString(), cellFont));
                table.addCell(new Phrase(tx.getDueDate().toLocalDate().toString(), cellFont));
                table.addCell(new Phrase(tx.getStatus().name(), cellFont));
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error generating circulation PDF report: " + e.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream generateInventoryCsvReport() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Book ID", "Title", "ISBN", "Author", "Category", "Publisher", "Year", "Total Copies", "Available Copies"))) {

            List<Book> books = bookRepository.findAll();
            for (Book b : books) {
                csvPrinter.printRecord(
                        b.getId(),
                        b.getTitle(),
                        b.getIsbn(),
                        b.getAuthor().getName(),
                        b.getCategory().getName(),
                        b.getPublisher(),
                        b.getPublishYear(),
                        b.getTotalCopies(),
                        b.getAvailableCopies()
                );
            }
            csvPrinter.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error generating inventory CSV report: " + e.getMessage());
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream generateOverdueCsvReport() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(out);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Fine ID", "Transaction ID", "Member Name", "Member Email", "Book Title", "Amount (₹)", "Fine Status", "Issued Date"))) {

            List<Fine> fines = fineRepository.findAll();
            for (Fine f : fines) {
                csvPrinter.printRecord(
                        f.getId(),
                        f.getTransaction().getId(),
                        f.getUser().getFullName(),
                        f.getUser().getEmail(),
                        f.getTransaction().getBookCopy().getBook().getTitle(),
                        f.getAmount(),
                        f.getStatus().name(),
                        f.getCreatedAt().toLocalDate().toString()
                );
            }
            csvPrinter.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error generating overdue CSV report: " + e.getMessage());
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
