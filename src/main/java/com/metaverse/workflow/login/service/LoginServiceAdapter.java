package com.metaverse.workflow.login.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoginServiceAdapter implements LoginService {

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    AgencyRepository agencyRepository;

    private String defaultPassword = "Password@123";

    @Override
    public LoginUserResponse getUserById(String id) {
        log.info("login service, userId : {}", id);
        Optional<User> user = loginRepository.findById(id);
        return user.isPresent() ? LoginUserResponseMapper.map(user.get()) : LoginUserResponse.builder().build();
    }

    @Override
    public WorkflowResponse createUser(LoginUserRequest request) {
        User user;
        if (request.getAgencyId() != null) {
            Optional<Agency> agency = agencyRepository.findById(request.getAgencyId());
            if (!agency.isPresent()) return WorkflowResponse.builder().status(400).message("Invalid Agency").build();
            user = loginRepository.save(User.builder().userId(request.getEmail()).email(request.getEmail()).firstName(request.getFirstName())
                    .lastName(request.getLastName()).mobileNo(request.getMobileNo()).userRole(request.getUserRole().name())
                    .gender(request.getGender()).password(defaultPassword).agency(agency.get()).build());
            return WorkflowResponse.builder().status(200).message("Success").data(LoginUserResponseMapper.map(user)).build();
        } else {
            user = loginRepository.save(User.builder().userId(request.getEmail()).email(request.getEmail()).firstName(request.getFirstName())
                    .lastName(request.getLastName()).mobileNo(request.getMobileNo()).userRole(request.getUserRole().name())
                    .gender(request.getGender()).password(defaultPassword).build());
            return WorkflowResponse.builder().status(200).message("Success").data(LoginUserResponseMapper.map(user)).build();
        }
    }

    @Override
    public WorkflowResponse updateUser(String userId, LoginUserRequest request) throws DataException {
        User user = loginRepository.findById(userId).orElseThrow(() -> new DataException("User does not exist with ID: "+userId, "USER-DATA-NOT-FOUND", 400));
        //user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMobileNo(request.getMobileNo());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());

        return WorkflowResponse.builder().status(200).message("User updated Successfully").data(LoginUserResponseMapper.map(loginRepository.save(user))).build();
    }

    @Override
    public List<LoginUserResponse> getUserByMobileNo(Long mobileNo) {
        return null;
    }

    @Override
    public WorkflowResponse getUsers() {
        log.info("users requested entered in service");
        List<User> userList = loginRepository.findAll();
        log.info("users list : " + userList.size());
        List<LoginUserResponse> response = userList != null ? userList.stream().map(user -> LoginUserResponseMapper.mapUser(user)).collect(Collectors.toList()) : null;
        return WorkflowResponse.builder().status(200).message("Success").data(response).build();
    }

    @Override
    public WorkflowResponse changePassword(ChangePasswordRequest request) {
        Optional<User> user = loginRepository.findById(request.getUserId());
        if (user.isPresent()) {
            User user1 = user.get();
            if (user1 != null && user1.getPassword().equals(request.getOldPassword())) {
                user1.setPassword(request.getNewPassword());
                User updatedUser = loginRepository.save(user1);
                return WorkflowResponse.builder().status(200).message("Success").data(LoginUserResponseMapper.map(updatedUser)).build();

            }
        }
        return WorkflowResponse.builder()
                .status(400)
                .message("Invalid User Name or Old Password")
                .build();
    }

}
