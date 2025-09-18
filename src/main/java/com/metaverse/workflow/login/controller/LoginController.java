package com.metaverse.workflow.login.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
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
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private ActivityLogService logService;

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
    public ResponseEntity<?> updateUser(Principal principal, @PathVariable String userId,
                                        @RequestBody LoginUserRequest request, HttpServletRequest servletRequest) {
        WorkflowResponse response ;
        try {
            response = loginService.updateUser(userId,request);
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
        logService.logs(principal.getName(), "UPDATE", "User details updated for userId: " + userId,"USER_MANAGEMENT",servletRequest.getRequestURI());
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
    public ResponseEntity<WorkflowResponse> changePassword(@RequestBody ChangePasswordRequest request,Principal principal,
                                                           HttpServletRequest servletRequest) throws DataException {
        log.info("Change password controller, userId: {}", request.getUserId());
        WorkflowResponse response = loginService.changePassword(request);
        logService.logs(principal.getName(), "CHANGE_PASSWORD",
                String.format("Password change for userId: %s | oldPwd: %s | newPwd: %s",
                request.getUserId(),
                request.getOldPassword(),
                request.getNewPassword()),
                "USER_MANAGEMENT",
                servletRequest.getRequestURI());
        return ResponseEntity.ok(response);
    }
}





