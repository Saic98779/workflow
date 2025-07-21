package com.metaverse.workflow.ProgramMonitoring.controller;

import com.metaverse.workflow.ProgramMonitoring.repository.ProgramMonitoringRepo;
import com.metaverse.workflow.ProgramMonitoring.service.ProgramMonitoringRequest;
import com.metaverse.workflow.ProgramMonitoring.service.ProgramMonitoringService;
import com.metaverse.workflow.common.logs.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/new")
public class ProgramMonitoringController {
    private final ProgramMonitoringService programService;
    private final ProgramMonitoringRepo feedbackRepository;
    private final ActivityLogService logService;
    @PostMapping("/program/feedback/save")
    public ResponseEntity<WorkflowResponse> saveFeedback(@RequestBody ProgramMonitoringRequest request)
    {
        logService.logs("Save","Program Monitoring", "Creating Program Monitoring Details");
        return ResponseEntity.ok(programService.saveFeedback(request));
    }
    @PostMapping("/program/feedback/update/{monitorId}")
    public ResponseEntity<?> updateFeedback(@PathVariable Long monitorId, @RequestBody ProgramMonitoringRequest request) {
        WorkflowResponse response;
        try {
            response = programService.updateFeedback(monitorId, request);
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
        logService.logs("Update","Program Monitoring", "Program Monitoring Details");
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

    @GetMapping("/program/details/for/feedback/{programId}")
    public ResponseEntity<?> getProgramDetailsFroFeedBack(@PathVariable Long programId) {
        WorkflowResponse response;
        try {
            response = programService.getProgramDetailsFroFeedBack(programId);
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProgramMonitoringFeedback(@PathVariable Long id) {
        logService.logs("Delete","Program Monitoring", "Deleting Program Monitoring details");
        return feedbackRepository.findById(id).map(feedback -> {
            feedbackRepository.delete(feedback);
            return ResponseEntity.ok("Program Monitoring FeedBack deleted successfully.");
        }).orElse(ResponseEntity.notFound().build());

    }
}
