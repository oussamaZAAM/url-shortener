package com.url.shortener.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AuthenticationRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAuthenticationRequest {

    /**
     * Returns the default error message to be displayed when the authentication request is in incorrect format.
     *
     * @return the default error message
     */
    String message() default "Authentication request is in incorrect format";

    /**
     * Returns an array of validation groups to which this constraint belongs.
     * Groups allow you to restrict the set of constraints applied during validation.
     *
     * @return an array of validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Returns an array of custom payload objects that provide metadata for the constraint.
     * Payloads are typically used to carry application-specific metadata with the constraint declaration.
     *
     * @return an array of custom payload objects
     */
    Class<? extends Payload>[] payload() default {};
}