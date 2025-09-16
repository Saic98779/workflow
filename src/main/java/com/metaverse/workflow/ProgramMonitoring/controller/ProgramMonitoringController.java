package com.metaverse.workflow.ProgramMonitoring.controller;

import com.metaverse.workflow.ProgramMonitoring.repository.ProgramMonitoringRepo;
import com.metaverse.workflow.ProgramMonitoring.service.ProgramMonitoringRequest;
import com.metaverse.workflow.ProgramMonitoring.service.ProgramMonitoringService;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/new")
public class ProgramMonitoringController {
    private final ProgramMonitoringService programService;
    private final ProgramMonitoringRepo feedbackRepository;
    private final ActivityLogService logService;
    @PostMapping("/program/feedback/save")
    public ResponseEntity<?> saveFeedback(Principal principal, @RequestBody ProgramMonitoringRequest request) throws DataException {
        WorkflowResponse response = programService.saveFeedback(request);
        logService.logs(principal.getName(), "save","saving program monitoring","Program Monitoring","/new/program/feedback/save");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/program/feedback/update/{monitorId}")
    public ResponseEntity<?> updateFeedback(Principal principal,@PathVariable Long monitorId, @RequestBody ProgramMonitoringRequest request) {
        WorkflowResponse response;
        try {
            response = programService.updateFeedback(monitorId, request);
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
        logService.logs(principal.getName(), "update","update program monitoring","Program Monitoring","/new/program/feedback/update/{monitorId}");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/program/feedback/{programId}")
    public ResponseEntity<?> getFeedbackByProgramId(@PathVariable Long programId) {
        WorkflowResponse response;
        try {
            response = programService.getFeedBackByProgramId(programId);
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/program/feedback/id/{feedBackId}")
    public ResponseEntity<?> getFeedbackById(@PathVariable Long feedBackId) {

        return ResponseEntity.ok(programService.getFeedBackById(feedBackId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProgramMonitoringFeedback(@PathVariable Long id,Principal principal) {
        return feedbackRepository.findById(id).map(feedback -> {
            feedbackRepository.delete(feedback);
            logService.logs(principal.getName(), "delete","delete program monitoring","Program Monitoring","/new/{id}");
            return ResponseEntity.ok("Program Monitoring FeedBack deleted successfully.");
        }).orElse(ResponseEntity.notFound().build());

    }
}
