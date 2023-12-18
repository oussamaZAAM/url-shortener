package com.url.shortener.services;

import com.url.shortener.exceptions.urls.NoUrlEntityException;
import com.url.shortener.models.UrlEntity;
import com.url.shortener.repositories.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UrlExpanderService {
    private final UrlRepository urlRepository;

    public UrlExpanderService (UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Cacheable(value = "shortUrl", key = "#shortUrl")
    public String expandShortUrl(String shortUrl) {
        Optional<UrlEntity> urlEntity = urlRepository.findByShortUrl(shortUrl);
        if (urlEntity.isEmpty()) {
            throw new NoUrlEntityException("Short url '" + shortUrl + "' is not in the database!");
        }
        return urlEntity.get().getOriginalUrl();
    }
}
