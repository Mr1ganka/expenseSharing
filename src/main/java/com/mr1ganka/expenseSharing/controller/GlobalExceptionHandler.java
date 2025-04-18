package com.mr1ganka.expenseSharing.controller;

import com.mr1ganka.expenseSharing.exception.UserAlreadyExistsException;
import com.mr1ganka.expenseSharing.exception.UserNotFoundException;
import com.mr1ganka.expenseSharing.utils.ErrorResponseClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.http.HttpRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseClass> handleUserAlreadyExists(HttpRequest req) {
        ErrorResponseClass response = new ErrorResponseClass(
                req.uri().getRawPath(),
                HttpStatus.CONFLICT.value(),
                "User already Exists"
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseClass> handleUserNotFoundExists(HttpRequest req) {
        ErrorResponseClass response = new ErrorResponseClass(
                req.uri().getRawPath(),
                HttpStatus.NOT_FOUND.value(),
                "User already Exists"
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseClass> handleBadCredentials (HttpRequest req, BadCredentialsException ex) {
        ErrorResponseClass responseClass = new ErrorResponseClass(
                req.uri().getRawPath(),
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage() != null ? ex.getMessage() : "Authorization Error"
        );
        return new ResponseEntity<>(responseClass, HttpStatus.UNAUTHORIZED);
    }
}
