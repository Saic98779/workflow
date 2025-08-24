package com.metaverse.workflow.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.common.util.ApplicationAPIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApplicationAPIResponse<?>> handleException(Exception ex) {
        ApplicationAPIResponse<?> response = ApplicationAPIResponse.builder()
                .message("Unexpected error: " + ex.getMessage())
                .success(false)
                .code("INTERNAL_SERVER_ERROR")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationAPIResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        ApplicationAPIResponse<?> response = ApplicationAPIResponse.builder()
                .message("Validation failed: " + errorMessage)
                .success(false)
                .code("BAD_REQUEST")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

