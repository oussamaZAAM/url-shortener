package com.url.shortener.unit.controllers;

import com.url.shortener.controllers.UrlShorteningController;
import com.url.shortener.dto.ErrorResponse;
import com.url.shortener.dto.ShortenedUrlResponse;
import com.url.shortener.exceptions.urls.UrlShorteningException;
import com.url.shortener.payload.OriginalUrlPayload;
import com.url.shortener.services.UrlShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UrlShorteningControllerTest {

    @Mock
    private UrlShortenerService urlShortenerService;

    @InjectMocks
    private UrlShorteningController urlShortening;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShortenUrl_Success() {
        // Arrange
        OriginalUrlPayload urlPayload = new OriginalUrlPayload("http://example.com");
        when(urlShortenerService.shortenUrl(urlPayload.getOriginalUrl())).thenReturn("http://short.url");

        // Act
        ResponseEntity<?> responseEntity = urlShortening.shortenUrl(urlPayload);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ShortenedUrlResponse);
        ShortenedUrlResponse responseBody = (ShortenedUrlResponse) responseEntity.getBody();
        assertEquals("http://short.url", responseBody.getShortUrl());

        // Verify that the urlShortenerService.shortenUrl method was called with the correct parameter
        verify(urlShortenerService, times(1)).shortenUrl("http://example.com");
    }

    @Test
    public void testShortenUrl_EmptyOriginalUrl() {
        // Create an example OriginalUrlPayload with an empty original URL
        OriginalUrlPayload urlPayload = new OriginalUrlPayload();
        urlPayload.setOriginalUrl("");

        // Call the controller method
        ResponseEntity<?> responseEntity = urlShortening.shortenUrl(urlPayload);

        // Verify the response for an empty original URL
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testShortenUrl_NullOriginalUrl() {
        // Create an example OriginalUrlPayload with a null original URL
        OriginalUrlPayload urlPayload = new OriginalUrlPayload();
        urlPayload.setOriginalUrl(null);

        // Call the controller method
        ResponseEntity<?> responseEntity = urlShortening.shortenUrl(urlPayload);

        // Verify the response for a null original URL
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testShortenUrl_ExpectationFailed() {
        // Arrange
        OriginalUrlPayload urlPayload = new OriginalUrlPayload("http://example.com");
        when(urlShortenerService.shortenUrl(any())).thenThrow(new UrlShorteningException("Failed to shorten URL"));

        // Act
        ResponseEntity<?> responseEntity = urlShortening.shortenUrl(urlPayload);

        // Assert
        assertEquals(HttpStatus.EXPECTATION_FAILED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof ErrorResponse);
        ErrorResponse responseBody = (ErrorResponse) responseEntity.getBody();
        assertEquals("Failed to shorten URL", responseBody.getMessage());

        // Verify that the urlShortenerService.shortenUrl method was called with the correct parameter
        verify(urlShortenerService, times(1)).shortenUrl("http://example.com");
    }
}