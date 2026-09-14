package com.library.backend.entity;

import com.library.backend.entity.enums.ReservationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "reserved_at", nullable = false)
    private LocalDateTime reservedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING;

    @Column(name = "queue_position")
    private Integer queuePosition;

    public Reservation() {}

    public Reservation(Long id, User user, Book book, LocalDateTime reservedAt, ReservationStatus status, Integer queuePosition) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.reservedAt = reservedAt;
        this.status = status != null ? status : ReservationStatus.PENDING;
        this.queuePosition = queuePosition;
    }

    @PrePersist
    protected void onCreate() {
        if (this.reservedAt == null) {
            this.reservedAt = LocalDateTime.now();
        }
    }

    public static ReservationBuilder builder() { return new ReservationBuilder(); }

    public static class ReservationBuilder {
        private Long id;
        private User user;
        private Book book;
        private LocalDateTime reservedAt;
        private ReservationStatus status = ReservationStatus.PENDING;
        private Integer queuePosition;

        public ReservationBuilder id(Long id) { this.id = id; return this; }
        public ReservationBuilder user(User user) { this.user = user; return this; }
        public ReservationBuilder book(Book book) { this.book = book; return this; }
        public ReservationBuilder reservedAt(LocalDateTime reservedAt) { this.reservedAt = reservedAt; return this; }
        public ReservationBuilder status(ReservationStatus status) { this.status = status; return this; }
        public ReservationBuilder queuePosition(Integer queuePosition) { this.queuePosition = queuePosition; return this; }

        public Reservation build() { return new Reservation(id, user, book, reservedAt, status, queuePosition); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public LocalDateTime getReservedAt() { return reservedAt; }
    public void setReservedAt(LocalDateTime reservedAt) { this.reservedAt = reservedAt; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
    public Integer getQueuePosition() { return queuePosition; }
    public void setQueuePosition(Integer queuePosition) { this.queuePosition = queuePosition; }
}
