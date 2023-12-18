package com.url.shortener.exceptions.urls;

public class UrlShorteningException extends RuntimeException{
    public UrlShorteningException(String message) {
        super(message);
    }
}
