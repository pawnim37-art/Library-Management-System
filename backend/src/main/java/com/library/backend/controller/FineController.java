package com.library.backend.controller;

import com.library.backend.dto.fine.FinePayRequest;
import com.library.backend.dto.fine.FineResponse;
import com.library.backend.entity.User;
import com.library.backend.repository.UserRepository;
import com.library.backend.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;
    private final UserRepository userRepository;

    @GetMapping("/my-fines")
    public ResponseEntity<List<FineResponse>> getMyFines(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(fineService.getUserFines(user.getId()));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LIBRARIAN')")
    public ResponseEntity<List<FineResponse>> getAllFines() {
        return ResponseEntity.ok(fineService.getAllFines());
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<FineResponse> payFine(@PathVariable Long id, @RequestBody FinePayRequest request) {
        return ResponseEntity.ok(fineService.payFine(id, request));
    }

    @PostMapping("/{id}/waive")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_LIBRARIAN')")
    public ResponseEntity<FineResponse> waiveFine(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        String reason = body != null ? body.get("reason") : "Administrative waiver";
        return ResponseEntity.ok(fineService.waiveFine(id, reason));
    }
}
