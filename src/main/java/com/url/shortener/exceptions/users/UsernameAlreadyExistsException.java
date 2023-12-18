package com.url.shortener.exceptions.users;


/**
 * Custom exception class for cases where a username already exists in the system.
 */
public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}

