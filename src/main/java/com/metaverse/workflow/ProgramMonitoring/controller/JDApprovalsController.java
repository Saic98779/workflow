package com.metaverse.workflow.ProgramMonitoring.controller;

import com.metaverse.workflow.ProgramMonitoring.dto.JDApprovalsDto;
import com.metaverse.workflow.ProgramMonitoring.service.JDApprovalsService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jd-approvals")
@RequiredArgsConstructor
public class JDApprovalsController {

    private final JDApprovalsService service;
    private final static Logger log = LogManager.getLogger(JDApprovalsController.class);


    @PostMapping(path = "/save")
    public ResponseEntity<WorkflowResponse> createApproval(@RequestBody JDApprovalsDto approvalsDto) {
        try {
            WorkflowResponse approval = service.createApproval(approvalsDto);
            log.info("Approval created successfully :  " + approval);
            return ResponseEntity.ok(approval);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder()
                            .status(400)
                            .message("Invalid request: " + e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping(path = "/get-all")
    public ResponseEntity<WorkflowResponse> getAll() {
        try {
            return ResponseEntity.ok(service.getAllApprovals());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder()
                            .status(400)
                            .message("Error fetching approvals: " + e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/{jdApprovalId}")
    public ResponseEntity<WorkflowResponse> getById(@PathVariable Long jdApprovalId) {
        try {
            return ResponseEntity.ok(service.getApprovalById(jdApprovalId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder()
                            .status(400)
                            .message("Invalid request: " + e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("/{jdApprovalId}")
    public ResponseEntity<WorkflowResponse> update(@PathVariable Long jdApprovalId, @RequestBody JDApprovalsDto approvalsDto) {
        try {
            log.info("Updating approval with id " + jdApprovalId);
            return ResponseEntity.ok(service.updateApproval(jdApprovalId, approvalsDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder()
                            .status(400)
                            .message("Update failed: " + e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/{jdApprovalId}")
    public ResponseEntity<WorkflowResponse> delete(@PathVariable Long jdApprovalId) {
        try {
            log.info("Deleting approval with id " + jdApprovalId);
            return ResponseEntity.ok(service.deleteApproval(jdApprovalId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder()
                            .status(400)
                            .message("Delete failed: " + e.getMessage())
                            .build()
            );
        }
    }
}
