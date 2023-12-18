package com.url.shortener.unit.services;
import com.url.shortener.exceptions.urls.NoUrlEntityException;
import com.url.shortener.models.UrlEntity;
import com.url.shortener.repositories.UrlRepository;
import com.url.shortener.services.UrlExpanderService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class UrlExpanderServiceTest {

    @Test
    public void testExpandShortUrl_ShortUrl_Exists() {
        // Mock the behavior of the repository
        UrlRepository urlRepository = mock(UrlRepository.class);

        // Create a sample UrlEntity with known short and original URLs
        UrlEntity sampleEntity = new UrlEntity();
        sampleEntity.setShortUrl("AwmPiR8gzuA");
        sampleEntity.setOriginalUrl("http://www.example.com");

        // Mock the behavior of findByShortUrl method
        when(urlRepository.findByShortUrl("AwmPiR8gzuA")).thenReturn(Optional.of(sampleEntity));

        // Create UrlShortenerService instance with the mocked repository
        UrlExpanderService urlExpanderService = new UrlExpanderService(urlRepository);

        // Test the service method
        String shortUrl = "AwmPiR8gzuA";
        String originalUrl = urlExpanderService.expandShortUrl(shortUrl);

        // Verify the result
        assertEquals("http://www.example.com", originalUrl);

        // Verify that the findByShortUrl method was called with the correct argument
        verify(urlRepository).findByShortUrl(shortUrl);
    }

    @Test
    public void testExpandShortUrl_ShortUrl_NotExist() {
        // Mock the behavior of the repository
        UrlRepository urlRepository = mock(UrlRepository.class);

        // Mock the behavior of findByShortUrl method for a non-existent short URL
        when(urlRepository.findByShortUrl("NonExistentShortUrl")).thenReturn(Optional.empty());

        // Create UrlExpanderService instance with the mocked repository
        UrlExpanderService urlExpanderService = new UrlExpanderService(urlRepository);

        // Test the service method with a non-existent short URL and expect a ShortUrlNotFoundException
        String nonExistentShortUrl = "NonExistentShortUrl";
        try {
            urlExpanderService.expandShortUrl(nonExistentShortUrl);
        } catch (NoUrlEntityException e) {
            // Verify that the findByShortUrl method was called with the correct argument
            verify(urlRepository).findByShortUrl(nonExistentShortUrl);
            assertEquals("Short url '" + nonExistentShortUrl + "' is not in the database!", e.getMessage());
        }
    }
}

