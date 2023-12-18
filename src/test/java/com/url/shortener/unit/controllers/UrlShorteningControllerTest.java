package com.url.shortener.unit.controllers;

import com.url.shortener.controllers.UrlShorteningController;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    public void testShortenUrl_Successful() {
        // Mock the behavior of the UrlShortenerService
        when(urlShortenerService.shortenUrl(any(String.class))).thenReturn("mockedShortUrl");

        // Create an example OriginalUrlPayload
        OriginalUrlPayload urlPayload = new OriginalUrlPayload();
        urlPayload.setOriginalUrl("http://www.example.com");

        // Call the controller method
        ResponseEntity<?> responseEntity = urlShortening.shortenUrl(urlPayload);

        // Verify the response
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(Map.of("shortUrl", "mockedShortUrl"), responseEntity.getBody());
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
    public void testShortenUrl_UrlShorteningException() {
        // Mock the behavior of the UrlShortenerService to throw an exception
        when(urlShortenerService.shortenUrl(any(String.class))).thenThrow(new UrlShorteningException("Error"));

        // Create an example OriginalUrlPayload
        OriginalUrlPayload urlPayload = new OriginalUrlPayload();
        urlPayload.setOriginalUrl("http://www.example.com");

        // Call the controller method
        ResponseEntity<?> responseEntity = urlShortening.shortenUrl(urlPayload);

        // Verify the response for a URL shortening exception
        assertEquals(HttpStatus.EXPECTATION_FAILED, responseEntity.getStatusCode());
        assertEquals(Map.of("message", "Error"), responseEntity.getBody());
    }
}