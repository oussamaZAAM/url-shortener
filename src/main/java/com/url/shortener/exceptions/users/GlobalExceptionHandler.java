package com.url.shortener.exceptions.users;

import com.url.shortener.exceptions.urls.NoUrlEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoUrlEntityException.class)
    public ResponseEntity<String> NoUrlEntityException(NoUrlEntityException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
