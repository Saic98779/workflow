package com.metaverse.workflow.login.service;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    private String userId;
    private String oldPassword;
    private String newPassword;
}
