package com.metaverse.workflow.security.controller;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.enums.UserRole;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.ApplicationAPIResponse;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.Agency;
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
    private final AgencyRepository agencyRepository;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApplicationAPIResponse<AuthenticationResponse>> register(@RequestBody RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (loginRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(
                    ApplicationAPIResponse.<AuthenticationResponse>builder()
                            .status(400)
                            .message("User with email already exists")
                            .data(null)
                            .build()
            );
        }

        Optional<Agency> agency = agencyRepository.findById(request.getAgencyId());
        if (request.getAgencyId() != null && agency.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    ApplicationAPIResponse.<AuthenticationResponse>builder()
                            .status(400)
                            .message("Invalid Agency")
                            .data(null)
                            .build()
            );
        }

        User savedUser = loginRepository.save(User.builder()
                .userId(request.getEmail())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .agency(agency.get())
                .gender(request.getGender())
                .mobileNo(request.getMobileNo())
                .userRole(request.getUserRole().name())
                .attempts(0)
                .status("ACTIVE")
                .build());

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(savedUser.getEmail())
                        .password(savedUser.getPassword())
                        .authorities("ROLE_" + savedUser.getUserRole())
                        .build()
        );

        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .token(token)
                .message("User registered successfully")
                .userId(savedUser.getUserId())
                .agencyId(agency.get().getAgencyId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .gender(savedUser.getGender())
                .mobileNo(savedUser.getMobileNo())
                .userRole(UserRole.valueOf(savedUser.getUserRole()))
                .agencyName(agency.map(Agency::getAgencyName).orElse(null))
                .build();

        return ResponseEntity.ok(
                ApplicationAPIResponse.<AuthenticationResponse>builder()
                        .status(200)
                        .message("Success")
                        .data(authResponse)
                        .build()
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user")
    public ResponseEntity<ApplicationAPIResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request) {
        log.info("Authenticating user with email: {}", request.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            User user = loginRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtService.generateToken(userDetails);
            log.info("User authenticated successfully: {}", user.getEmail());

            AuthenticationResponse userData = AuthenticationResponse.builder()
                    .token(token)
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .agencyId(user.getAgency() != null ? user.getAgency().getAgencyId() : null)
                    .agencyName(user.getAgency() != null ? user.getAgency().getAgencyName() : null)
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .gender(user.getGender())
                    .mobileNo(user.getMobileNo())
                    .userRole(UserRole.valueOf(user.getUserRole()))
                    .build();

            ApplicationAPIResponse<AuthenticationResponse> response = ApplicationAPIResponse.<AuthenticationResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(userData)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Authentication failed for user {}: {}", request.getEmail(), e.getMessage());
            ApplicationAPIResponse<AuthenticationResponse> errorResponse = ApplicationAPIResponse.<AuthenticationResponse>builder()
                    .status(403)
                    .message("Authentication failed")
                    .data(null)
                    .build();
            return ResponseEntity.status(403).body(errorResponse);
        }
    }

}