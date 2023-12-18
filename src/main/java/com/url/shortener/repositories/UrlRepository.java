package com.url.shortener.repositories;

import com.url.shortener.models.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UrlRepository extends MongoRepository<UrlEntity, String> {
    Optional<UrlEntity> findByOriginalUrl(String originalUrl);
    Optional<UrlEntity> findByShortUrl(String shortUrl);
}
