package com.metaverse.workflow.security.controller;

import com.metaverse.workflow.common.enums.UserRole;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.User;
import com.metaverse.workflow.security.dto.AuthenticationRequest;
import com.metaverse.workflow.security.dto.AuthenticationResponse;
import com.metaverse.workflow.security.dto.RegisterRequest;
import com.metaverse.workflow.security.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());
        
        // Check if user already exists
        if (loginRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("User with email {} already exists", request.getEmail());
            return ResponseEntity.badRequest().build();
        }

        // Create new user
        User user = User.builder()
                .userId(request.getEmail())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .mobileNo(request.getMobileNo())
                .address(request.getAddress())
                .userRole(request.getUserRole().name())
                .attempts(0)
                .status("ACTIVE")
                .build();

        // Save user
        User savedUser = loginRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());

        // Generate JWT token
        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(savedUser.getEmail())
                        .password(savedUser.getPassword())
                        .authorities("ROLE_" + savedUser.getUserRole())
                        .build()
        );

        // Return response
        return ResponseEntity.ok(
                AuthenticationResponse.builder()
                        .token(token)
                        .userId(savedUser.getUserId())
                        .email(savedUser.getEmail())
                        .firstName(savedUser.getFirstName())
                        .lastName(savedUser.getLastName())
                        .userRole(UserRole.valueOf(savedUser.getUserRole()))
                        .build()
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        log.info("Authenticating user with email: {}", request.getEmail());
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Get authenticated user
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Get user from database
            User user = loginRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate JWT token
            String token = jwtService.generateToken(userDetails);
            log.info("User authenticated successfully: {}", user.getEmail());

            // Return response
            return ResponseEntity.ok(
                    AuthenticationResponse.builder()
                            .token(token)
                            .userId(user.getUserId())
                            .email(user.getEmail())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .userRole(UserRole.valueOf(user.getUserRole()))
                            .build()
            );
        } catch (Exception e) {
            log.error("Authentication failed for user {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(403).build();
        }
    }
}