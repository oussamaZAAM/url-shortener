package com.url.shortener.controllers;

import com.url.shortener.dto.ErrorResponse;
import com.url.shortener.dto.ShortenedUrlResponse;
import com.url.shortener.exceptions.urls.UrlShorteningException;
import com.url.shortener.payload.OriginalUrlPayload;
import com.url.shortener.services.UrlShortenerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Controller
@Slf4j
public class UrlShorteningController {

    private final UrlShortenerService urlShortenerService;

    public UrlShorteningController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    @ResponseBody
    public ResponseEntity<?> shortenUrl(@RequestBody OriginalUrlPayload url) {
        try {
            if (isInvalidUrl(url)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            String shortUrl = urlShortenerService.shortenUrl(url.getOriginalUrl());
            ShortenedUrlResponse response = new ShortenedUrlResponse(shortUrl);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (UrlShorteningException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.EXPECTATION_FAILED);
        }
    }

    private boolean isInvalidUrl(OriginalUrlPayload url) {
        return url == null || url.getOriginalUrl() == null || url.getOriginalUrl().isEmpty();
    }
}
