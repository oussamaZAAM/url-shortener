package com.url.shortener.integration.services;

import com.url.shortener.exceptions.users.InvalidUsernameException;
import com.url.shortener.exceptions.users.UsernameAlreadyExistsException;
import com.url.shortener.integration.config.TestMongoConfig;
import com.url.shortener.models.User;
import com.url.shortener.payload.RegistrationRequest;
import com.url.shortener.repositories.UserRepository;
import com.url.shortener.services.RegistrationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestMongoConfig.class)
class RegistrationServiceIntegrationTest {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testRegister_SuccessfulRegistration() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("testpassword");

        // Act
        String result = registrationService.register(registrationRequest);

        // Assert
        assertEquals("User registered successfully!", result);

        // Verify that the user was saved in the database
        User savedUser = userRepository.findByUsername("testuser").orElse(null);
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
        assertTrue(passwordEncoder.matches("testpassword", savedUser.getPassword()));
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("testpassword");

        registrationService.register(registrationRequest);

        RegistrationRequest registrationRequest2 = new RegistrationRequest();
        registrationRequest2.setUsername("testuser");
        registrationRequest2.setEmail("test@example.com");
        registrationRequest2.setPassword("testpassword");

        // Act and Assert
        assertThrows(UsernameAlreadyExistsException.class,
                () -> registrationService.register(registrationRequest2));
    }

    @Test
    void testRegister_InvalidUsername() {
        // Arrange
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("xx");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("testpassword");

        // Act and Assert
        assertThrows(InvalidUsernameException.class,
                () -> registrationService.register(registrationRequest));
    }

    // Add more test methods for other scenarios (e.g., email already exists, invalid email, etc.)
}
