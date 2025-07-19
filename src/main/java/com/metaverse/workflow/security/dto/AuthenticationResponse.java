package com.metaverse.workflow.security.dto;

import com.metaverse.workflow.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole userRole;
}