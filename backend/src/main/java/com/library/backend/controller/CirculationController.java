package com.library.backend.controller;

import com.library.backend.dto.circulation.*;
import com.library.backend.entity.User;
import com.library.backend.repository.UserRepository;
import com.library.backend.service.CirculationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/circulation")
@RequiredArgsConstructor
public class CirculationController {

    private final CirculationService circulationService;
    private final UserRepository userRepository;

    @PostMapping("/issue")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LIBRARIAN')")
    public ResponseEntity<TransactionResponse> issueBook(@Valid @RequestBody IssueRequest request) {
        return ResponseEntity.ok(circulationService.issueBook(request));
    }

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<TransactionResponse> borrowBook(@AuthenticationPrincipal UserDetails userDetails,
                                                          @PathVariable Long bookId) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        IssueRequest request = new IssueRequest();
        request.setUserId(user.getId());
        request.setBookId(bookId);
        request.setLoanDays(14);
        return ResponseEntity.ok(circulationService.issueBook(request));
    }

    @PostMapping("/return")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LIBRARIAN')")
    public ResponseEntity<TransactionResponse> returnBook(@Valid @RequestBody ReturnRequest request) {
        return ResponseEntity.ok(circulationService.returnBook(request));
    }

    @PostMapping("/renew/{transactionId}")
    public ResponseEntity<TransactionResponse> renewBook(@PathVariable Long transactionId) {
        return ResponseEntity.ok(circulationService.renewBook(transactionId));
    }

    @PostMapping("/reserve/{bookId}")
    public ResponseEntity<ReservationResponse> reserveBook(@AuthenticationPrincipal UserDetails userDetails,
                                                             @PathVariable Long bookId) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(circulationService.reserveBook(user.getId(), bookId));
    }

    @DeleteMapping("/reserve/{reservationId}")
    public ResponseEntity<Map<String, String>> cancelReservation(@AuthenticationPrincipal UserDetails userDetails,
                                                                 @PathVariable Long reservationId) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        circulationService.cancelReservation(reservationId, user.getId());
        return ResponseEntity.ok(Map.of("message", "Reservation cancelled successfully"));
    }

    @GetMapping("/my-loans")
    public ResponseEntity<List<TransactionResponse>> getMyLoans(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(circulationService.getUserTransactions(user.getId()));
    }

    @GetMapping("/my-reservations")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(circulationService.getUserReservations(user.getId()));
    }

    @GetMapping("/all-loans")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LIBRARIAN')")
    public ResponseEntity<List<TransactionResponse>> getAllLoans() {
        return ResponseEntity.ok(circulationService.getAllTransactions());
    }

    @GetMapping("/all-reservations")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LIBRARIAN')")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return ResponseEntity.ok(circulationService.getAllReservations());
    }
}
