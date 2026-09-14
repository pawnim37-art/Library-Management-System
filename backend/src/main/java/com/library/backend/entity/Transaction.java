package com.library.backend.entity;

import com.library.backend.entity.enums.TransactionStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_copy_id", nullable = false)
    private BookCopy bookCopy;

    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "renewal_count", nullable = false)
    private Integer renewalCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.ISSUED;

    public Transaction() {}

    public Transaction(Long id, User user, BookCopy bookCopy, LocalDateTime issueDate, LocalDateTime dueDate, LocalDateTime returnDate, Integer renewalCount, TransactionStatus status) {
        this.id = id;
        this.user = user;
        this.bookCopy = bookCopy;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.renewalCount = renewalCount != null ? renewalCount : 0;
        this.status = status != null ? status : TransactionStatus.ISSUED;
    }

    public static TransactionBuilder builder() { return new TransactionBuilder(); }

    public static class TransactionBuilder {
        private Long id;
        private User user;
        private BookCopy bookCopy;
        private LocalDateTime issueDate;
        private LocalDateTime dueDate;
        private LocalDateTime returnDate;
        private Integer renewalCount = 0;
        private TransactionStatus status = TransactionStatus.ISSUED;

        public TransactionBuilder id(Long id) { this.id = id; return this; }
        public TransactionBuilder user(User user) { this.user = user; return this; }
        public TransactionBuilder bookCopy(BookCopy bookCopy) { this.bookCopy = bookCopy; return this; }
        public TransactionBuilder issueDate(LocalDateTime issueDate) { this.issueDate = issueDate; return this; }
        public TransactionBuilder dueDate(LocalDateTime dueDate) { this.dueDate = dueDate; return this; }
        public TransactionBuilder returnDate(LocalDateTime returnDate) { this.returnDate = returnDate; return this; }
        public TransactionBuilder renewalCount(Integer renewalCount) { this.renewalCount = renewalCount; return this; }
        public TransactionBuilder status(TransactionStatus status) { this.status = status; return this; }

        public Transaction build() {
            return new Transaction(id, user, bookCopy, issueDate, dueDate, returnDate, renewalCount, status);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public BookCopy getBookCopy() { return bookCopy; }
    public void setBookCopy(BookCopy bookCopy) { this.bookCopy = bookCopy; }
    public LocalDateTime getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDateTime issueDate) { this.issueDate = issueDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
    public Integer getRenewalCount() { return renewalCount; }
    public void setRenewalCount(Integer renewalCount) { this.renewalCount = renewalCount; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
}
