package com.url.shortener.exceptions.users;

/**
 * Custom exception class for cases where there is an error with user registration.
 */
public class RegistrationException extends RuntimeException {
    public RegistrationException(String message) {
        super(message);
    }
}
