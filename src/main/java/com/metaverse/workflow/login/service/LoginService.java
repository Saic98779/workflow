package com.metaverse.workflow.login.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;

import java.util.List;

public interface LoginService {

    LoginUserResponse getUserById(String id);

    WorkflowResponse createUser(LoginUserRequest request);
    WorkflowResponse updateUser(String userId,LoginUserRequest request) throws DataException;
    List<LoginUserResponse> getUserByMobileNo(Long mobileNo);

    WorkflowResponse getUsers();
    WorkflowResponse changePassword(ChangePasswordRequest request) ;
}
