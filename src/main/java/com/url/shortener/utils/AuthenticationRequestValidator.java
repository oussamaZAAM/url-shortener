package com.url.shortener.utils;

import com.url.shortener.payload.AuthenticationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuthenticationRequestValidator implements ConstraintValidator<ValidAuthenticationRequest, AuthenticationRequest> {

    @Override
    public void initialize(ValidAuthenticationRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(AuthenticationRequest request, ConstraintValidatorContext context) {
        if (request.getUsername() == null || request.getUsername().isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank()) {
            return false;
        }
        return true;
    }
}
