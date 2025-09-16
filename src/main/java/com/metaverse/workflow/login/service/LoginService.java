package com.metaverse.workflow.login.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;

public interface LoginService {

    LoginUserResponse getUserById(String id);
    WorkflowResponse updateUser(String userId,LoginUserRequest request) throws DataException;
    WorkflowResponse getUsers();
    WorkflowResponse changePassword(ChangePasswordRequest request) ;
}
