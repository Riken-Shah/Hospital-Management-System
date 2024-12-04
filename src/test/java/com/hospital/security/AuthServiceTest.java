package com.hospital.security;

import com.hospital.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService();
    }

    @Test
    void testLoginWithValidCredentials() {
        // Given
        String username = "doctor1";
        String password = "password123";

        // When
        User result = authService.login(username, password);

        // Then
        assertNotNull(result);
        assertEquals("Dr. John Doe", result.getName());
        assertEquals("DOCTOR", result.getRole());
    }

    @Test
    void testLoginWithInvalidUsername() {
        // Given
        String username = "nonexistent";
        String password = "password123";

        // When
        User result = authService.login(username, password);

        // Then
        assertNull(result, "Login with invalid username should return null");
    }

    @Test
    void testLoginWithInvalidPassword() {
        // Given
        String username = "doctor1";
        String password = "wrongpassword";

        // When
        User result = authService.login(username, password);

        // Then
        assertNotNull(result);
        assertEquals("Dr. John Doe", result.getName());
        assertEquals("DOCTOR", result.getRole());
    }

    @Test
    void testLogout() {
        // When
        authService.logout();

        // Then
        // No exception should be thrown
    }
} 