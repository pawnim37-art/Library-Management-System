package com.library.backend.service;

import com.library.backend.config.JwtUtils;
import com.library.backend.dto.auth.AuthResponse;
import com.library.backend.dto.auth.RegisterRequest;
import com.library.backend.entity.User;
import com.library.backend.repository.FineRepository;
import com.library.backend.repository.TransactionRepository;
import com.library.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private TransactionRepository transactionRepository;
    @Mock private FineRepository fineRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtils jwtUtils;
    @Mock private AuthenticationManager authenticationManager;

    private AuthService authService;
    private RegisterRequest sampleMemberRequest;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userRepository,
                transactionRepository,
                fineRepository,
                passwordEncoder,
                jwtUtils,
                authenticationManager
        );

        sampleMemberRequest = new RegisterRequest(
                "john.doe@example.com",
                "securePassword123",
                "John Doe",
                "+1 555-0199",
                "123 Library St",
                "MEMBER",
                ""
        );
    }

    @Test
    @DisplayName("Should successfully register a new member and return JWT token")
    void testRegisterNewMemberSuccess() {
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(false);
        when(passwordEncoder.encode("securePassword123")).thenReturn("encodedPassword");
        when(jwtUtils.generateToken(eq("john.doe@example.com"), anyString())).thenReturn("mock-jwt-token");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        AuthResponse response = authService.register(sampleMemberRequest);

        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals("John Doe", response.getFullName());
        assertEquals("ROLE_MEMBER", response.getRole());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when email is already registered")
    void testRegisterDuplicateEmailThrowsException() {
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.register(sampleMemberRequest);
        });

        assertEquals("Email is already registered!", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when registering admin without valid secret key")
    void testRegisterAdminInvalidSecretKeyThrowsException() {
        RegisterRequest adminRequest = new RegisterRequest(
                "admin@library.com",
                "adminPass123",
                "Admin User",
                "+1 555-0200",
                "Admin HQ",
                "ADMIN",
                "WRONG_SECRET"
        );

        when(userRepository.existsByEmail("admin@library.com")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.register(adminRequest);
        });

        assertEquals("Invalid secret key for elevated role registration!", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
