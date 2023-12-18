package com.url.shortener.exceptions.users;

/**
 * Custom exception class for cases where an email already exists in the system.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}

