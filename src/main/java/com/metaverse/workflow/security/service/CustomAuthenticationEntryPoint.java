package com.metaverse.workflow.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.common.util.ApplicationAPIResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApplicationAPIResponse<?> apiResponse = ApplicationAPIResponse.builder()
                .message("Authentication failed: " + authException.getMessage())
                .success(false)
                .code("UNAUTHORIZED")
                .timestamp(LocalDateTime.now())
                .build();
        
        objectMapper.findAndRegisterModules(); // For LocalDateTime serialization
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}