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
public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    private Long mobileNo;
    private String address;
    private UserRole userRole;
}