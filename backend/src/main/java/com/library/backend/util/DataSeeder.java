package com.library.backend.util;

import com.library.backend.entity.*;
import com.library.backend.entity.enums.*;
import com.library.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final TransactionRepository transactionRepository;
    private final ReservationRepository reservationRepository;
    private final FineRepository fineRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return; // Data already seeded
        }

        // 1. Seed Users
        User admin = User.builder()
                .email("admin@library.com")
                .password(passwordEncoder.encode("admin123"))
                .fullName("System Administrator")
                .phone("+1 555-0100")
                .address("100 Tech Blvd, Suite 400")
                .role(Role.ROLE_ADMIN)
                .active(true)
                .build();

        User librarian = User.builder()
                .email("librarian@library.com")
                .password(passwordEncoder.encode("librarian123"))
                .fullName("Sarah Jenkins (Librarian)")
                .phone("+1 555-0101")
                .address("12 Central Library Ave")
                .role(Role.ROLE_LIBRARIAN)
                .active(true)
                .build();

        User member1 = User.builder()
                .email("member@library.com")
                .password(passwordEncoder.encode("member123"))
                .fullName("John Doe")
                .phone("+1 555-0102")
                .address("45 Oak Street, Apt 3B")
                .role(Role.ROLE_MEMBER)
                .active(true)
                .build();

        User member2 = User.builder()
                .email("alice@library.com")
                .password(passwordEncoder.encode("member123"))
                .fullName("Alice Smith")
                .phone("+1 555-0103")
                .address("88 Maple Drive")
                .role(Role.ROLE_MEMBER)
                .active(true)
                .build();

        userRepository.saveAll(List.of(admin, librarian, member1, member2));

        // 2. Seed Authors
        Author robertMartin = authorRepository.save(Author.builder().name("Robert C. Martin").biography("Uncle Bob has been a software professional since 1970.").build());
        Author martinKleppmann = authorRepository.save(Author.builder().name("Martin Kleppmann").biography("Researcher in distributed systems at University of Cambridge.").build());
        Author frankHerbert = authorRepository.save(Author.builder().name("Frank Herbert").biography("Acclaimed American science-fiction author of Dune.").build());
        Author harperLee = authorRepository.save(Author.builder().name("Harper Lee").biography("Pulitzer Prize winning author of To Kill a Mockingbird.").build());
        Author yuvalHarari = authorRepository.save(Author.builder().name("Yuval Noah Harari").biography("Historian, philosopher, and bestselling author.").build());
        Author jamesClear = authorRepository.save(Author.builder().name("James Clear").biography("Writer and speaker focused on habits and decision making.").build());
        Author andyWeir = authorRepository.save(Author.builder().name("Andy Weir").biography("Novelist and former computer programmer.").build());

        // 3. Seed Categories
        Category cs = categoryRepository.save(Category.builder().name("Computer Science").department("Technology").description("Software engineering, system architecture, and algorithms.").build());
        Category sciFi = categoryRepository.save(Category.builder().name("Science Fiction").department("Fiction").description("Futuristic science, space exploration, and speculative worlds.").build());
        Category classics = categoryRepository.save(Category.builder().name("Classics").department("Literature").description("Timeless literary masterpieces and historic fiction.").build());
        Category history = categoryRepository.save(Category.builder().name("History").department("Non-Fiction").description("World history, anthropology, and human evolution.").build());
        Category selfHelp = categoryRepository.save(Category.builder().name("Self-Help").department("General").description("Personal development, habits, and productivity.").build());

        // 4. Seed Books & Physical Copies
        Book b1 = bookRepository.save(Book.builder()
                .title("Clean Code: A Handbook of Agile Software Craftsmanship")
                .isbn("978-0132350884")
                .author(robertMartin)
                .category(cs)
                .publisher("Prentice Hall")
                .publishYear(2008)
                .description("Even bad code can function. But if code isn't clean, it can bring a development organization to its knees.")
                .coverImageUrl("https://images.unsplash.com/photo-1532012197267-da84d127e765?w=500&q=80")
                .totalCopies(3)
                .availableCopies(2)
                .build());

        Book b2 = bookRepository.save(Book.builder()
                .title("Designing Data-Intensive Applications")
                .isbn("978-1449373320")
                .author(martinKleppmann)
                .category(cs)
                .publisher("O'Reilly Media")
                .publishYear(2017)
                .description("The definitive guide to data systems architecture, scalability, consistency, and reliability.")
                .coverImageUrl("https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500&q=80")
                .totalCopies(4)
                .availableCopies(3)
                .build());

        Book b3 = bookRepository.save(Book.builder()
                .title("Dune")
                .isbn("978-0441172719")
                .author(frankHerbert)
                .category(sciFi)
                .publisher("Chilton Books")
                .publishYear(1965)
                .description("Set on the desert planet Arrakis, Dune is the story of the boy Paul Atreides, who will become Muad'Dib.")
                .coverImageUrl("https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=500&q=80")
                .totalCopies(3)
                .availableCopies(1)
                .build());

        Book b4 = bookRepository.save(Book.builder()
                .title("To Kill a Mockingbird")
                .isbn("978-0061120084")
                .author(harperLee)
                .category(classics)
                .publisher("J. B. Lippincott & Co.")
                .publishYear(1960)
                .description("The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it.")
                .coverImageUrl("https://images.unsplash.com/photo-1512820790803-83ca734da794?w=500&q=80")
                .totalCopies(2)
                .availableCopies(2)
                .build());

        Book b5 = bookRepository.save(Book.builder()
                .title("Sapiens: A Brief History of Humankind")
                .isbn("978-0062316097")
                .author(yuvalHarari)
                .category(history)
                .publisher("Harper")
                .publishYear(2014)
                .description("100,000 years ago, at least six human species inhabited the earth. Today there is just one. Us. Homo sapiens.")
                .coverImageUrl("https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=500&q=80")
                .totalCopies(3)
                .availableCopies(3)
                .build());

        Book b6 = bookRepository.save(Book.builder()
                .title("Atomic Habits")
                .isbn("978-0735211292")
                .author(jamesClear)
                .category(selfHelp)
                .publisher("Avery")
                .publishYear(2018)
                .description("An easy and proven way to build good habits and break bad ones.")
                .coverImageUrl("https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=500&q=80")
                .totalCopies(3)
                .availableCopies(2)
                .build());

        Book b7 = bookRepository.save(Book.builder()
                .title("Project Hail Mary")
                .isbn("978-0593135204")
                .author(andyWeir)
                .category(sciFi)
                .publisher("Ballantine Books")
                .publishYear(2021)
                .description("A lone astronaut must save the earth from a disaster in this incredible science fiction thriller.")
                .coverImageUrl("https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=500&q=80")
                .totalCopies(2)
                .availableCopies(2)
                .build());

        // Create copies for B1
        BookCopy c1_1 = bookCopyRepository.save(BookCopy.builder().book(b1).barcode("BC-1-1").status(BookCopyStatus.BORROWED).conditionNote("Good").build());
        BookCopy c1_2 = bookCopyRepository.save(BookCopy.builder().book(b1).barcode("BC-1-2").status(BookCopyStatus.AVAILABLE).conditionNote("Excellent").build());
        BookCopy c1_3 = bookCopyRepository.save(BookCopy.builder().book(b1).barcode("BC-1-3").status(BookCopyStatus.AVAILABLE).conditionNote("Good").build());

        // Copies for B2
        BookCopy c2_1 = bookCopyRepository.save(BookCopy.builder().book(b2).barcode("BC-2-1").status(BookCopyStatus.BORROWED).conditionNote("New").build());
        BookCopy c2_2 = bookCopyRepository.save(BookCopy.builder().book(b2).barcode("BC-2-2").status(BookCopyStatus.AVAILABLE).conditionNote("New").build());
        BookCopy c2_3 = bookCopyRepository.save(BookCopy.builder().book(b2).barcode("BC-2-3").status(BookCopyStatus.AVAILABLE).conditionNote("New").build());
        BookCopy c2_4 = bookCopyRepository.save(BookCopy.builder().book(b2).barcode("BC-2-4").status(BookCopyStatus.AVAILABLE).conditionNote("New").build());

        // Copies for B3
        BookCopy c3_1 = bookCopyRepository.save(BookCopy.builder().book(b3).barcode("BC-3-1").status(BookCopyStatus.BORROWED).conditionNote("Slight Wear").build());
        BookCopy c3_2 = bookCopyRepository.save(BookCopy.builder().book(b3).barcode("BC-3-2").status(BookCopyStatus.BORROWED).conditionNote("Good").build());
        BookCopy c3_3 = bookCopyRepository.save(BookCopy.builder().book(b3).barcode("BC-3-3").status(BookCopyStatus.AVAILABLE).conditionNote("New").build());

        // Copies for B6
        BookCopy c6_1 = bookCopyRepository.save(BookCopy.builder().book(b6).barcode("BC-6-1").status(BookCopyStatus.BORROWED).conditionNote("Good").build());
        BookCopy c6_2 = bookCopyRepository.save(BookCopy.builder().book(b6).barcode("BC-6-2").status(BookCopyStatus.AVAILABLE).conditionNote("Good").build());
        BookCopy c6_3 = bookCopyRepository.save(BookCopy.builder().book(b6).barcode("BC-6-3").status(BookCopyStatus.AVAILABLE).conditionNote("New").build());

        // 5. Seed Transactions (Loans)
        LocalDateTime now = LocalDateTime.now();

        // Transaction 1: Active Loan (Clean Code) for Member1
        Transaction tx1 = transactionRepository.save(Transaction.builder()
                .user(member1)
                .bookCopy(c1_1)
                .issueDate(now.minusDays(5))
                .dueDate(now.plusDays(9))
                .renewalCount(0)
                .status(TransactionStatus.ISSUED)
                .build());

        // Transaction 2: OVERDUE Loan (Designing Data-Intensive Applications) for Member1
        Transaction tx2 = transactionRepository.save(Transaction.builder()
                .user(member1)
                .bookCopy(c2_1)
                .issueDate(now.minusDays(20))
                .dueDate(now.minusDays(6))
                .renewalCount(0)
                .status(TransactionStatus.OVERDUE)
                .build());

        // Overdue Fine for tx2 (6 days * ₹0.50 = ₹3.00)
        fineRepository.save(Fine.builder()
                .transaction(tx2)
                .user(member1)
                .amount(new BigDecimal("3.00"))
                .status(FineStatus.PENDING)
                .reason("Overdue fine for 6 days late return of 'Designing Data-Intensive Applications'")
                .createdAt(now.minusDays(6))
                .build());

        // Transaction 3: Active Loan (Dune) for Alice
        Transaction tx3 = transactionRepository.save(Transaction.builder()
                .user(member2)
                .bookCopy(c3_1)
                .issueDate(now.minusDays(2))
                .dueDate(now.plusDays(12))
                .renewalCount(1)
                .status(TransactionStatus.ISSUED)
                .build());

        // 6. Seed Reservations
        reservationRepository.save(Reservation.builder()
                .user(member1)
                .book(b3) // Dune
                .reservedAt(now.minusDays(1))
                .status(ReservationStatus.PENDING)
                .queuePosition(1)
                .build());

        // 7. Seed Notifications
        notificationRepository.save(Notification.builder()
                .user(member1)
                .title("Overdue Notice: Designing Data-Intensive Applications")
                .message("Your loan for 'Designing Data-Intensive Applications' was due on " + now.minusDays(6).toLocalDate() + ". Please return it promptly. Fine accrued: ₹3.00.")
                .type(NotificationType.OVERDUE)
                .isRead(false)
                .createdAt(now.minusDays(5))
                .build());

        notificationRepository.save(Notification.builder()
                .user(member1)
                .title("Welcome to Library Management System")
                .message("Your member account has been activated! Explore our catalog and manage your borrowings online.")
                .type(NotificationType.INFO)
                .isRead(true)
                .createdAt(now.minusDays(10))
                .build());
    }
}
