package com.url.shortener.exceptions.urls;

public class NoUrlEntityException extends RuntimeException{
    public NoUrlEntityException(String message) {
        super(message);
    }
}
