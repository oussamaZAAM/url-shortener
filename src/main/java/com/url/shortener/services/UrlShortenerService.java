package com.url.shortener.services;

import com.url.shortener.exceptions.urls.UrlShorteningException;
import com.url.shortener.models.UrlEntity;
import com.url.shortener.repositories.UrlRepository;
import com.url.shortener.utils.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.url.shortener.utils.Base62Converter.convertToBase62;

@Service
@Slf4j
public class UrlShortenerService {

    private final UrlRepository urlRepository;

    public UrlShortenerService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String shortenUrl(String originalUrl) {
        Optional<UrlEntity> searchByOriginalUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (searchByOriginalUrl.isPresent()) {
            return buildShortUrl(searchByOriginalUrl.get().getShortUrl());
        } else {
            // Create a new UrlEntity with the original URL
            UrlEntity urlEntity = new UrlEntity();
            urlEntity.setOriginalUrl(originalUrl);

            // Generate id (using Snowflake) and assign it to urlEntity.id
            SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1);
            long snowflakeId = generator.generateSnowflakeId();
            urlEntity.setId(String.valueOf(snowflakeId));

            // Generate a Base62 Hash and assign it as shortened Url
            String hash = convertToBase62(snowflakeId);
            if (hash.isEmpty()) throw new UrlShorteningException("Server Error. Please try again!");
            urlEntity.setShortUrl(hash);

            // Save the UrlEntity
            UrlEntity savedEntity = urlRepository.save(urlEntity);
            // Return the short URL (which is the mocked ID in this case)
            return buildShortUrl(savedEntity.getShortUrl());
        }
    }

    private String buildShortUrl(String shortUrl) {
        return "http://localhost:8080/" + shortUrl;
    }
}
