package com.metaverse.workflow.login.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.login.service.ChangePasswordRequest;
import com.metaverse.workflow.login.service.LoginService;
import com.metaverse.workflow.login.service.LoginUserRequest;
import com.metaverse.workflow.login.service.LoginUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class LoginController {

    @Autowired
    LoginService loginService;

    @Operation(summary = "Get user by id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = LoginUserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @GetMapping(value = "/login/user", produces = {"application/json"})
    public ResponseEntity<LoginUserResponse> getUserById(@RequestHeader("userId") String userId) {
        log.info("login controller, userId : {}", userId);
        LoginUserResponse response = loginService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user by id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = LoginUserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/login/user/update/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody LoginUserRequest request) {
        WorkflowResponse response ;
        try {
            response = loginService.updateUser(userId,request);
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Get user by id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = LoginUserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/login/user/create", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<WorkflowResponse> createUser(@RequestBody LoginUserRequest request) {
        log.info("login controller, userId : {}", request.getMobileNo());
        WorkflowResponse response = loginService.createUser(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user by id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = LoginUserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @GetMapping(value = "/login", produces = {"application/json"})
    public ResponseEntity<WorkflowResponse> login(@RequestHeader("userId") String userId, @RequestHeader("password") String password) {
        log.info("login controller, userId : {}", userId);
        LoginUserResponse response = loginService.getUserById(userId);
        if (response.getUserId() != null && response.getPassword().equals(password)) {
            response.setPassword(null);
            return ResponseEntity.ok(WorkflowResponse.builder().status(200).message("Success").data(response).build());

        } else {
            return ResponseEntity.ok(WorkflowResponse.builder().status(400).message("User not found").build());
        }
    }

    @Operation(summary = "Get all users", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = WorkflowResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @GetMapping(value = "/login/allusrs", produces = {"application/json"})
    public ResponseEntity<WorkflowResponse> users() {
        log.info("users requested entered");
        WorkflowResponse response = loginService.getUsers();
        log.info("users requested closed success fully");
        return ResponseEntity.ok(response);

    }

    @Operation(summary = "Change user password", responses = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully",
                    content = @Content(schema = @Schema(implementation = WorkflowResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user or password",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PutMapping(value = "/login/change-password", produces = {"application/json"})
    public ResponseEntity<WorkflowResponse> changePassword(@RequestBody ChangePasswordRequest request) throws DataException {
        log.info("Change password controller, userId: {}", request.getUserId());
        return ResponseEntity.ok(loginService.changePassword(request));
    }
}





