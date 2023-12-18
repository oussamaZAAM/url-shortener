package com.url.shortener.unit.controllers;

import com.url.shortener.controllers.AuthController;
import com.url.shortener.exceptions.users.EmailAlreadyExistsException;
import com.url.shortener.exceptions.users.InvalidEmailException;
import com.url.shortener.exceptions.users.InvalidUsernameException;
import com.url.shortener.exceptions.users.UsernameAlreadyExistsException;
import com.url.shortener.payload.RegistrationRequest;
import com.url.shortener.services.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_SuccessfulRegistration() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("testpassword");

        when(registrationService.register(any(RegistrationRequest.class))).thenReturn("Registration successful");

        // Act
        ResponseEntity<String> response = authController.register(registrationRequest);

        // Assert
        assertEquals("Registration successful", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(registrationService, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("existingUser");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("testpassword");

        when(registrationService.register(any(RegistrationRequest.class)))
                .thenThrow(new UsernameAlreadyExistsException("Username already exists"));

        // Act
        ResponseEntity<String> response = authController.register(registrationRequest);

        // Assert
        assertEquals("Username already exists", response.getBody());
        assertEquals(500, response.getStatusCodeValue());
        verify(registrationService, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    void testRegister_InvalidUsername() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("invalid-username");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("testpassword");

        when(registrationService.register(any(RegistrationRequest.class)))
                .thenThrow(new InvalidUsernameException("Invalid username"));

        // Act
        ResponseEntity<String> response = authController.register(registrationRequest);

        // Assert
        assertEquals("Invalid username", response.getBody());
        assertEquals(400, response.getStatusCodeValue());
        verify(registrationService, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("newUser");
        registrationRequest.setEmail("existing@example.com");
        registrationRequest.setPassword("testpassword");

        when(registrationService.register(any(RegistrationRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("Email already exists"));

        // Act
        ResponseEntity<String> response = authController.register(registrationRequest);

        // Assert
        assertEquals("Email already exists", response.getBody());
        assertEquals(500, response.getStatusCodeValue());
        verify(registrationService, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    void testRegister_InvalidEmail() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("newUser");
        registrationRequest.setEmail("invalid-email");
        registrationRequest.setPassword("testpassword");

        when(registrationService.register(any(RegistrationRequest.class)))
                .thenThrow(new InvalidEmailException("Invalid email"));

        // Act
        ResponseEntity<String> response = authController.register(registrationRequest);

        // Assert
        assertEquals("Invalid email", response.getBody());
        assertEquals(400, response.getStatusCodeValue());
        verify(registrationService, times(1)).register(any(RegistrationRequest.class));
    }
}
