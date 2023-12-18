package com.url.shortener.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.url.shortener.config.WebSecurityConfig;
import com.url.shortener.controllers.UrlShorteningController;
import com.url.shortener.exceptions.urls.UrlShorteningException;
import com.url.shortener.services.UrlShortenerService;
import com.url.shortener.payload.OriginalUrlPayload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(UrlShorteningController.class)
@Import(WebSecurityConfig.class)
class UrlShorteningControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlShortenerService urlShortenerService;

    @Test
    void testShortenUrl_SuccessfulShortening() throws Exception {
        // Arrange
        String originalUrl = "http://www.example.com";
        when(urlShortenerService.shortenUrl(any(String.class))).thenReturn("http://short.url");

        OriginalUrlPayload payload = new OriginalUrlPayload();
        payload.setOriginalUrl(originalUrl);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.shortUrl").value("http://short.url"));
    }

    @Test
    void testShortenUrl_InvalidOriginalUrl() throws Exception {
        // Arrange
        OriginalUrlPayload payload = new OriginalUrlPayload();
        payload.setOriginalUrl(null);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testShortenUrl_ExceptionHandling() throws Exception {
        // Arrange
        String originalUrl = "http://www.example.com";
        when(urlShortenerService.shortenUrl(any(String.class)))
                .thenThrow(new UrlShorteningException("Error shortening URL"));

        OriginalUrlPayload payload = new OriginalUrlPayload();
        payload.setOriginalUrl(originalUrl);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error shortening URL"));
    }
}

