package com.library.backend.service;

import com.library.backend.dto.fine.FinePayRequest;
import com.library.backend.dto.fine.FineResponse;
import com.library.backend.entity.Fine;
import com.library.backend.entity.enums.FineStatus;
import com.library.backend.entity.enums.NotificationType;
import com.library.backend.repository.FineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;
    private final NotificationService notificationService;

    public List<FineResponse> getAllFines() {
        return fineRepository.findAll().stream()
                .map(this::mapToFineResponse)
                .collect(Collectors.toList());
    }

    public List<FineResponse> getUserFines(Long userId) {
        return fineRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToFineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FineResponse payFine(Long fineId, FinePayRequest request) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new IllegalArgumentException("Fine not found with ID: " + fineId));

        if (fine.getStatus() != FineStatus.PENDING) {
            throw new IllegalStateException("Fine is already " + fine.getStatus());
        }

        fine.setStatus(FineStatus.PAID);
        fine.setPaidAt(LocalDateTime.now());
        if (request.getReason() != null) {
            fine.setReason(fine.getReason() + " [Paid via " + request.getPaymentMethod() + "]");
        }

        Fine saved = fineRepository.save(fine);

        notificationService.createNotification(
                fine.getUser(),
                "Fine Payment Processed",
                "Payment of ₹" + fine.getAmount() + " received for '" + fine.getTransaction().getBookCopy().getBook().getTitle() + "'.",
                NotificationType.INFO
        );

        return mapToFineResponse(saved);
    }

    @Transactional
    public FineResponse waiveFine(Long fineId, String reason) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new IllegalArgumentException("Fine not found with ID: " + fineId));

        if (fine.getStatus() != FineStatus.PENDING) {
            throw new IllegalStateException("Fine is already " + fine.getStatus());
        }

        fine.setStatus(FineStatus.WAIVED);
        fine.setReason("WAIVED by Librarian: " + (reason != null ? reason : "Administrative courtesy"));
        Fine saved = fineRepository.save(fine);

        notificationService.createNotification(
                fine.getUser(),
                "Fine Waived",
                "Your fine of ₹" + fine.getAmount() + " has been waived by library staff.",
                NotificationType.INFO
        );

        return mapToFineResponse(saved);
    }

    private FineResponse mapToFineResponse(Fine fine) {
        return FineResponse.builder()
                .id(fine.getId())
                .transactionId(fine.getTransaction().getId())
                .userId(fine.getUser().getId())
                .userName(fine.getUser().getFullName())
                .userEmail(fine.getUser().getEmail())
                .bookTitle(fine.getTransaction().getBookCopy().getBook().getTitle())
                .amount(fine.getAmount())
                .status(fine.getStatus().name())
                .reason(fine.getReason())
                .createdAt(fine.getCreatedAt())
                .paidAt(fine.getPaidAt())
                .build();
    }
}
