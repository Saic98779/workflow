package com.metaverse.workflow.exceptions;

import com.metaverse.workflow.notifications.exceptions.AgencyNotFoundException;
import com.metaverse.workflow.notifications.exceptions.ParticipantNotFoundException;
import com.metaverse.workflow.notifications.exceptions.ProgramNotFoundException;
import com.metaverse.workflow.notifications.exceptions.UserNotFoundException;
import com.metaverse.workflow.security.ApplicationAPIResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.LocalDateTime;

@RestControllerAdvice
@Order
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationAbstractException.class)
    public ResponseEntity<Object> handleApplicationException(ApplicationAbstractException ex, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setTitle("Application Error");
        problemDetail.setProperty("code", ex.getErrorCode());
        problemDetail.setProperty("success", false);
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), status, request);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Access denied: " + ex.getMessage());
        problemDetail.setTitle("Forbidden");
        problemDetail.setProperty("code", "FORBIDDEN");
        problemDetail.setProperty("success", false);
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        problemDetail.setTitle("Unauthorized");
        problemDetail.setProperty("code", "UNAUTHORIZED");
        problemDetail.setProperty("success", false);
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Authentication failed");
        problemDetail.setTitle("Unauthorized");
        problemDetail.setProperty("code", "UNAUTHORIZED");
        problemDetail.setProperty("success", false);
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid argument: " + ex.getMessage());
        problemDetail.setTitle("Bad Request");
        problemDetail.setProperty("code", "BAD_REQUEST");
        problemDetail.setProperty("success", false);
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("User Not Found");
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(AgencyNotFoundException.class)
    public ProblemDetail handleAgencyNotFound(AgencyNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Agency Not Found");
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setProperty("code", "INTERNAL_SERVER_ERROR");
        problemDetail.setProperty("success", false);
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ProgramNotFoundException.class)
    public ProblemDetail handleProgramNotFound(AgencyNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Program Not Found");
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(ParticipantNotFoundException.class)
    public ProblemDetail handleParticipantNotFound(AgencyNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Participant Not Found");
        problemDetail.setDetail(ex.getMessage());
        return problemDetail;
    }

}
