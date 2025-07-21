package com.metaverse.workflow.counsellor.controller;

import com.metaverse.workflow.common.logs.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.counsellor.service.CounsellorRegistrationRequest;
import com.metaverse.workflow.counsellor.service.CounsellorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class CounsellorController {

    @Autowired
    CounsellorService counsellorService;
    @Autowired
    private ActivityLogService logService;


    @PostMapping("/saveCounsellor")
    public ResponseEntity<WorkflowResponse> saveCounsellor(@RequestBody CounsellorRegistrationRequest counsellorRequest) {
        log.info("Save counsellor");
        WorkflowResponse response = counsellorService.saveCounseller(counsellorRequest);
        logService.logs("Save","Counsellor","Adding Counsellor Data");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCounsellorByAllocatedMandal/{id}")
    public ResponseEntity<WorkflowResponse> getCounsellorByMandal(@PathVariable Integer id) {
        WorkflowResponse counsellorResponse = counsellorService.getCounsellerByMandal(id);
        return ResponseEntity.ok(counsellorResponse);
    }
    @GetMapping("/getAllCounsellors")
    public ResponseEntity<WorkflowResponse> getAllCounsellors()
    {
        WorkflowResponse response=counsellorService.getAllCounsellors();
        return ResponseEntity.ok(response);
    }


}
