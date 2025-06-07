package com.metaverse.workflow.login.service;

import com.metaverse.workflow.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class LoginUserRequest {

    private String email;
    private UserRole userRole;
    private String firstName;
    private String lastName;
    private String gender;
    private Long mobileNo;
    private String address;
    private Long agencyId;

}
