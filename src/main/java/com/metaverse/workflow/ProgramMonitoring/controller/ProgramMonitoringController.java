package com.metaverse.workflow.ProgramMonitoring.controller;

import com.metaverse.workflow.ProgramMonitoring.repository.ProgramMonitoringRepo;
import com.metaverse.workflow.ProgramMonitoring.service.ProgramMonitoringRequest;
import com.metaverse.workflow.ProgramMonitoring.service.ProgramMonitoringService;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final static Logger log = LogManager.getLogger(ProgramMonitoringController.class);
    @PostMapping("/program/feedback/save")
    public ResponseEntity<?> saveFeedback(Principal principal, @RequestBody ProgramMonitoringRequest request, HttpServletRequest servletRequest) throws DataException {
        WorkflowResponse response = programService.saveFeedback(request);
        logService.logs(principal.getName(), "SAVE","saving program monitoring","Program Monitoring", servletRequest.getRequestURI());
        log.info("saved program monitoring");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/program/feedback/update/{monitorId}")
    public ResponseEntity<?> updateFeedback(Principal principal,@PathVariable Long monitorId,
                                            @RequestBody ProgramMonitoringRequest request,
                                            HttpServletRequest servletRequest) {
        WorkflowResponse response;
        try {
            response = programService.updateFeedback(monitorId, request);
            log.info("updated program monitoring");
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
        logService.logs(principal.getName(), "UPDATE","update program monitoring","Program Monitoring", servletRequest.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/program/feedback/{programId}")
    public ResponseEntity<?> getFeedbackByProgramId(@PathVariable Long programId) {
        WorkflowResponse response;
        try {
            response = programService.getFeedBackByProgramId(programId);
            log.info("get program monitoring");
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
    public ResponseEntity<String> deleteProgramMonitoringFeedback(@PathVariable Long id,Principal principal,
                                                                  HttpServletRequest servletRequest) {
        return feedbackRepository.findById(id).map(feedback -> {
            feedbackRepository.delete(feedback);
            logService.logs(principal.getName(), "DELETE","delete program monitoring","Program Monitoring", servletRequest.getRequestURI());
            log.info("deleted program monitoring"+principal.getName());
            return ResponseEntity.ok("Program Monitoring FeedBack deleted successfully.");
        }).orElse(ResponseEntity.notFound().build());

    }
    @GetMapping("/program/monitoring-dropdown/{programId}")
    public ResponseEntity<?> getFeedbackByProgramIdDropDown(@PathVariable Long programId) {
        WorkflowResponse response;
        try {
            response = programService.getFeedBackByProgramIdDropDown(programId);
            log.info("get program monitoring");
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
        return ResponseEntity.ok(response);
    }
}
