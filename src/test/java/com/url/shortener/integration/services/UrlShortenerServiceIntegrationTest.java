package com.url.shortener.integration.services;

import com.url.shortener.models.UrlEntity;
import com.url.shortener.repositories.UrlRepository;
import com.url.shortener.services.UrlShortenerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class UrlShortenerServiceIntegrationTest {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @Autowired
    private UrlRepository urlRepository;


    @Test
    public void testShortenUrl_SuccessfulShortening() {
        // Arrange
        String originalUrl = "http://www.example.com";

        // Act
        String shortUrl = urlShortenerService.shortenUrl(originalUrl);

        // Assert
        // Check if the short URL is not empty and follows the expected format
        assertEquals(shortUrl, urlRepository.findByOriginalUrl(originalUrl)
                .map(entity -> "http://localhost:8080/" + entity.getShortUrl())
                .orElse(null));
    }

    @Test
    public void testShortenUrl_UrlAlreadyExists() {
        // Arrange
        String originalUrl = "http://www.example.com";

        // Save a URL entity with the same original URL before shortening
        UrlEntity existingEntity = new UrlEntity();
        existingEntity.setOriginalUrl(originalUrl);
        existingEntity.setShortUrl("existingShortUrl");
        urlRepository.save(existingEntity);

        // Act
        String shortUrl = urlShortenerService.shortenUrl(originalUrl);

        // Assert
        // Check if the short URL is the same as the existing entity's short URL
        assertEquals("http://localhost:8080/existingShortUrl", shortUrl);
    }
}
