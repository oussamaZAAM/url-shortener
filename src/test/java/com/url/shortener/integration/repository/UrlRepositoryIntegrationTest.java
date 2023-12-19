package com.url.shortener.integration.repository;

import com.url.shortener.integration.config.TestMongoConfig;
import com.url.shortener.models.UrlEntity;
import com.url.shortener.repositories.UrlRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@Import(TestMongoConfig.class)
public class UrlRepositoryIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UrlRepository urlRepository;

    @AfterEach
    void tearDown() {
        urlRepository.deleteAll();
    }

    @Test
    void findByOriginalUrl_ShouldReturnUrlEntity() {
        // Arrange
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl("http://example1.com");
        urlEntity.setShortUrl("short1");

        // Save the entity to the in-memory MongoDB database
        mongoTemplate.save(urlEntity);

        // Act
        Optional<UrlEntity> result = urlRepository.findByOriginalUrl("http://example1.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("http://example1.com", result.get().getOriginalUrl());
        assertEquals("short1", result.get().getShortUrl());
    }

    @Test
    void findByShortUrl_ShouldReturnUrlEntity() {
        // Arrange
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl("http://example2.com");
        urlEntity.setShortUrl("short2");

        // Save the entity to the in-memory MongoDB database
        mongoTemplate.save(urlEntity);

        // Act
        Optional<UrlEntity> result = urlRepository.findByShortUrl("short2");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("http://example2.com", result.get().getOriginalUrl());
        assertEquals("short2", result.get().getShortUrl());
    }
}
