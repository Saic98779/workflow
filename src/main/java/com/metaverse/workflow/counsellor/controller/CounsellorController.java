package com.metaverse.workflow.counsellor.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.counsellor.service.CounsellorRegistrationRequest;
import com.metaverse.workflow.counsellor.service.CounsellorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class CounsellorController {

    private static final Logger log = LogManager.getLogger(CounsellorController.class);
    private final CounsellorService counsellorService;
    private final ActivityLogService logService;


    @PostMapping("/saveCounsellor")
    public ResponseEntity<WorkflowResponse> saveCounsellor(Principal principal, @RequestBody CounsellorRegistrationRequest counsellorRequest, HttpServletRequest servletRequest) {
        WorkflowResponse response = counsellorService.saveCounseller(counsellorRequest);
        log.info("Counsellor saved successfully");
        logService.logs(principal.getName(), "SAVE", "Counsellor saved successfully", "Counsellor", servletRequest.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCounsellorByAllocatedMandal/{id}")
    public ResponseEntity<WorkflowResponse> getCounsellorByMandal(@PathVariable Integer id) {
        WorkflowResponse counsellorResponse = counsellorService.getCounsellerByMandal(id);
        log.info("Fetched counsellor for mandalId={}", id);
        return ResponseEntity.ok(counsellorResponse);
    }

    @GetMapping("/getAllCounsellors")
    public ResponseEntity<WorkflowResponse> getAllCounsellors() {
        WorkflowResponse response = counsellorService.getAllCounsellors();
        log.info("Fetched all counsellors successfully");
        return ResponseEntity.ok(response);
    }
}
