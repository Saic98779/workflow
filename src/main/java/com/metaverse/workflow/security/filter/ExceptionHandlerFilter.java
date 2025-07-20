package com.metaverse.workflow.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.common.util.ApplicationAPIResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Global exception handler filter for security filters
 * This filter catches exceptions thrown by security filters and returns a standardized error response
 */
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            // Let Spring Security handle authentication exceptions
            throw ex;
        } catch (RuntimeException ex) {
            setErrorResponse(response, ex);
        }
    }

    private void setErrorResponse(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApplicationAPIResponse<?> apiResponse = ApplicationAPIResponse.builder()
                .message("An error occurred during request processing: " + ex.getMessage())
                .success(false)
                .code("INTERNAL_SERVER_ERROR")
                .timestamp(LocalDateTime.now())
                .build();

        objectMapper.findAndRegisterModules(); // For LocalDateTime serialization
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}