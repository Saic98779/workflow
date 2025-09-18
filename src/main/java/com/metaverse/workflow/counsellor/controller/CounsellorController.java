package com.metaverse.workflow.counsellor.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.counsellor.service.CounsellorRegistrationRequest;
import com.metaverse.workflow.counsellor.service.CounsellorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@Slf4j
public class CounsellorController {

    @Autowired
    private CounsellorService counsellorService;
    @Autowired
    private ActivityLogService logService;


    @PostMapping("/saveCounsellor")
    public ResponseEntity<WorkflowResponse> saveCounsellor(Principal principal, @RequestBody CounsellorRegistrationRequest counsellorRequest, HttpServletRequest servletRequest) {
        WorkflowResponse response = counsellorService.saveCounseller(counsellorRequest);
        logService.logs(principal.getName(), "SAVE","counsellor saved successfully","Counsellor", servletRequest.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCounsellorByAllocatedMandal/{id}")
    public ResponseEntity<WorkflowResponse> getCounsellorByMandal(@PathVariable Integer id) {
        WorkflowResponse counsellorResponse = counsellorService.getCounsellerByMandal(id);
        return ResponseEntity.ok(counsellorResponse);
    }

    @GetMapping("/getAllCounsellors")
    public ResponseEntity<WorkflowResponse> getAllCounsellors() {
        WorkflowResponse response = counsellorService.getAllCounsellors();
        return ResponseEntity.ok(response);
    }


}
