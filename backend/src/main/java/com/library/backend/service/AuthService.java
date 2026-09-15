package com.library.backend.service;

import com.library.backend.config.JwtUtils;
import com.library.backend.dto.auth.*;
import com.library.backend.entity.User;
import com.library.backend.entity.enums.FineStatus;
import com.library.backend.entity.enums.Role;
import com.library.backend.repository.FineRepository;
import com.library.backend.repository.TransactionRepository;
import com.library.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final FineRepository fineRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       TransactionRepository transactionRepository,
                       FineRepository fineRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.fineRepository = fineRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered!");
        }

        Role role = Role.ROLE_MEMBER;
        if (request.getRole() != null && !request.getRole().isBlank()) {
            String roleStr = request.getRole().trim().toUpperCase();
            if (!roleStr.startsWith("ROLE_")) {
                roleStr = "ROLE_" + roleStr;
            }
            if (roleStr.equals("ROLE_ADMIN") || roleStr.equals("ROLE_LIBRARIAN")) {
                // Secret key protection for elevated registration
                if (!"LIBRARY_SECRET_2026".equals(request.getSecretKey())) {
                    throw new IllegalArgumentException("Invalid secret key for elevated role registration!");
                }
                role = Role.valueOf(roleStr);
            }
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(role)
                .active(true)
                .build();

        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .build();
    }

    public UserProfileDTO getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        int activeLoans = transactionRepository.findActiveTransactionsByUserId(user.getId()).size();
        int totalLoans = transactionRepository.findByUserIdOrderByIssueDateDesc(user.getId()).size();

        BigDecimal pendingFines = fineRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .filter(f -> f.getStatus() == FineStatus.PENDING)
                .map(f -> f.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return UserProfileDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole().name())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .activeLoansCount(activeLoans)
                .totalBorrowCount(totalLoans)
                .pendingFinesAmount(pendingFines)
                .build();
    }

    @Transactional
    public UserProfileDTO updateProfile(String email, UserProfileDTO request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }

        userRepository.save(user);
        return getProfile(email);
    }
}
