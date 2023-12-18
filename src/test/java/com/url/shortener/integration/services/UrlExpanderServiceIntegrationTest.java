package com.url.shortener.integration.services;

import com.url.shortener.exceptions.urls.NoUrlEntityException;
import com.url.shortener.models.UrlEntity;
import com.url.shortener.repositories.UrlRepository;
import com.url.shortener.services.UrlExpanderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UrlExpanderServiceIntegrationTest {

    @Autowired
    private UrlExpanderService urlExpanderService;

    @MockBean
    private UrlRepository urlRepository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testExpandShortUrl_SuccessfulExpansion() {
        // Arrange
        String shortUrl = "mockedShortUrl";
        String originalUrl = "http://www.example.com";

        // Mock the behavior of the repository
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setShortUrl(shortUrl);
        urlEntity.setOriginalUrl(originalUrl);
        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(urlEntity));

        // Act
        String expandedUrl = urlExpanderService.expandShortUrl(shortUrl);

        // Assert
        // Check if the expanded URL matches the original URL
        assertEquals(originalUrl, expandedUrl);
    }

    @Test
    public void testExpandShortUrl_UrlNotInDatabase() {
        // Arrange
        String shortUrl = "nonExistentShortUrl";

        // Mock the behavior of the repository
        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

        // Act and Assert
        // Check if NoUrlEntityException is thrown for a non-existent short URL
        try {
            urlExpanderService.expandShortUrl(shortUrl);
        } catch (NoUrlEntityException e) {
            // Verify the exception message or any additional assertions if needed
            assertEquals("Short url 'nonExistentShortUrl' is not in the database!", e.getMessage());
        }
    }
}
