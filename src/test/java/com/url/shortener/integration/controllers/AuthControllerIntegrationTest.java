package com.url.shortener.integration.controllers;

import com.url.shortener.config.WebSecurityConfig;
import com.url.shortener.controllers.AuthController;
import com.url.shortener.controllers.UrlForwardingController;
import com.url.shortener.exceptions.users.UsernameAlreadyExistsException;
import com.url.shortener.payload.RegistrationRequest;
import com.url.shortener.services.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AuthController.class)
@Import(WebSecurityConfig.class)
public class AuthControllerIntegrationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @Test
    void testRegister() throws Exception {
        // Mock the behavior of the registrationService
        when(registrationService.register(any(RegistrationRequest.class))).thenReturn("Registration successful");

        // Create a sample RegistrationRequest
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("testpassword");

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Registration successful"));

        // You can add more assertions based on your specific use case
    }

    @Test
    void testRegisterValidation() throws Exception {
        // Create an invalid RegistrationRequest (missing required fields)
        RegistrationRequest invalidRequest = new RegistrationRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testRegisterException() throws Exception {
        // Create a sample RegistrationRequest
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("testpassword");

        // Mock the behavior of the registrationService to simulate an unexpected exception
        when(registrationService.register(any(RegistrationRequest.class)))
                .thenThrow(new UsernameAlreadyExistsException("Username already exists"));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("Username already exists"));
    }

    @Test
    void testRegisterInvalidInputFormat() throws Exception {
        // Invalid JSON structure
        String invalidJson = "{\"invalidField\": \"value\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testRegisterEmptyEmail() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setEmail("testuser");
        registrationRequest.setPassword("testpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//                .andExpect(MockMvcResultMatchers.content().string("Email is not valid!"));
    }

    @Test
    void testRegisterNullPassword() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
