package com.url.shortener.controllers;

import com.url.shortener.services.UrlExpanderService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Controller
public class UrlForwardingController {
    private final UrlExpanderService urlExpanderService;

    public UrlForwardingController(UrlExpanderService urlExpanderService) {
        this.urlExpanderService = urlExpanderService;
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> expandUrl(@PathVariable(value = "shortUrl") String shortUrl, HttpServletResponse response) throws IOException {
        if (shortUrl == null || shortUrl.isEmpty()) {
            // Handle the case when the short URL is null
            response.sendError(HttpStatus.NOT_FOUND.value(), "Short URL cannot be null");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String originalUrl = urlExpanderService.expandShortUrl(shortUrl);

        if (originalUrl != null) {
            // Redirect to the original URL with a 302 status code
            response.sendRedirect(originalUrl);
            return new ResponseEntity<>(HttpStatus.FOUND);
        } else {
            // Handle the case when the short URL does not exist
            response.sendError(HttpStatus.NOT_FOUND.value(), "Short URL not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
