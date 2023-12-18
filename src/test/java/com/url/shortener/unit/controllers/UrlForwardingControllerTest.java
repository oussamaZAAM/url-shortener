package com.url.shortener.unit.controllers;

import com.url.shortener.controllers.UrlForwardingController;
import com.url.shortener.services.UrlExpanderService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UrlForwardingControllerTest {

    @Mock
    private UrlExpanderService urlExpanderService;

    @InjectMocks
    private UrlForwardingController urlForwarding;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExpandUrlWithValidShortUrl() throws IOException {
        // Mock the behavior of the UrlExpanderService
        when(urlExpanderService.expandShortUrl("validShortUrl")).thenReturn("http://www.example.com");

        // Call the controller method
        ResponseEntity<String> responseEntity = urlForwarding.expandUrl("validShortUrl", response);

        // Verify the response
        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());

        // Verify that sendRedirect was called with the correct URL
        verify(response).sendRedirect("http://www.example.com");
    }

    @Test
    public void testExpandUrlWithInvalidShortUrl() throws IOException {
        // Mock the behavior of the UrlExpanderService for an invalid short URL
        when(urlExpanderService.expandShortUrl("invalidShortUrl")).thenReturn(null);

        // Call the controller method
        ResponseEntity<String> responseEntity = urlForwarding.expandUrl("invalidShortUrl", response);

        // Verify the response for an invalid short URL
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        // Ensure that sendError was called instead
        verify(response).sendError(HttpStatus.NOT_FOUND.value(), "Short URL not found");
    }

    @Test
    public void testExpandUrlWithNullShortUrl() throws IOException {
        // Call the controller method with a null short URL
        ResponseEntity<String> responseEntity = urlForwarding.expandUrl(null, response);

        // Verify the response for a null short URL
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        // Ensure that sendError was called instead
        verify(response).sendError(HttpStatus.NOT_FOUND.value(), "Short URL cannot be null");
    }

    @Test
    public void testExpandUrlWithEmptyShortUrl() throws IOException {
        // Call the controller method with an empty short URL
        ResponseEntity<String> responseEntity = urlForwarding.expandUrl("", response);

        // Verify the response for an empty short URL
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        // Ensure that sendError was called instead
        verify(response).sendError(HttpStatus.NOT_FOUND.value(), "Short URL cannot be null");
    }
}