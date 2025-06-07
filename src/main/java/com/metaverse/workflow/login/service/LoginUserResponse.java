package com.metaverse.workflow.login.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.metaverse.workflow.common.enums.UserRole;
import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginUserResponse {

    private String userId;
    private Long agencyId;
    private String email;
    private String password;
    private UserRole userRole;
    private Integer attempts;
    private String status;
    private Date createdOn;
    private Date updatedOn;
    private String firstName;
    private String lastName;
    private String gender;
    private Long mobileNo;
    private String address;
    private String agencyName;

}
