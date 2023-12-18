package com.url.shortener.dto;

public class ShortenedUrlResponse {

    private String shortUrl;

    public ShortenedUrlResponse(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
