package com.url.shortener.unit.services;

import com.url.shortener.models.UrlEntity;
import com.url.shortener.repositories.UrlRepository;
import com.url.shortener.services.UrlShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UrlShortenerServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShortenUrl_ExistingUrl() {
        // Mock the behavior of the repository for an existing URL
        String originalUrl = "http://www.example.com";
        UrlEntity existingEntity = new UrlEntity();
        existingEntity.setShortUrl("existingShortUrl");
        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.of(existingEntity));

        // Test the service method for an existing URL
        String shortUrl = urlShortenerService.shortenUrl(originalUrl);

        // Verify the result
        assertEquals("http://localhost:8080/existingShortUrl", shortUrl);
    }

    @Test
    public void testShortenUrl_NewUrl() {
        // Mock the behavior of the repository for a new URL
        String originalUrl = "http://www.example.com";
        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(Optional.empty());

        // Mock the behavior of the repository when saving a new URL
        UrlEntity savedEntity = new UrlEntity();
        savedEntity.setShortUrl("newShortUrl");
        when(urlRepository.save(any(UrlEntity.class))).thenReturn(savedEntity);

        // Test the service method for a new URL
        String shortUrl = urlShortenerService.shortenUrl(originalUrl);

        // Verify the result
        assertEquals("http://localhost:8080/newShortUrl", shortUrl);
    }

    // Add more test cases as needed
}
