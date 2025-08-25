package com.metaverse.workflow.exceptions;

import com.metaverse.workflow.security.ApplicationAPIResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Order
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationAbstractException.class)
    public ResponseEntity<ApplicationAPIResponse<?>> handleApplicationException(
            ApplicationAbstractException ex, WebRequest request) {
        return new ResponseEntity<>(
                ApplicationAPIResponse.builder()
                        .message(ex.getMessage())
                        .success(false)
                        .code(ex.getErrorCode())
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.valueOf(ex.getStatusCode())
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApplicationAPIResponse<?>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        return new ResponseEntity<>(
                ApplicationAPIResponse.builder()
                        .message("Access denied: " + ex.getMessage())
                        .success(false)
                        .code("FORBIDDEN")
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApplicationAPIResponse<?>> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {
        return new ResponseEntity<>(
                ApplicationAPIResponse.builder()
                        .message("Invalid credentials: " + ex.getMessage())
                        .success(false)
                        .code("UNAUTHORIZED")
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApplicationAPIResponse<?>> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {
        return new ResponseEntity<>(
                ApplicationAPIResponse.builder()
                        .message("Authentication failed: " + ex.getMessage())
                        .success(false)
                        .code("UNAUTHORIZED")
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApplicationAPIResponse<?>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(
                ApplicationAPIResponse.builder()
                        .message("Invalid argument: " + ex.getMessage())
                        .success(false)
                        .code("BAD_REQUEST")
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApplicationAPIResponse<?>> handleAllUncaughtException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                ApplicationAPIResponse.builder()
                        .message("An unexpected error occurred: " + ex.getMessage())
                        .success(false)
                        .code("INTERNAL_SERVER_ERROR")
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
