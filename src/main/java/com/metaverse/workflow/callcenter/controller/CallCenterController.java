package com.metaverse.workflow.callcenter.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.callcenter.service.CallCenterService;
import com.metaverse.workflow.callcenter.service.CallCenterVerificationRequest;
import com.metaverse.workflow.callcenter.service.QuestionRequest;
import com.metaverse.workflow.callcenter.service.SubActivityQuestionsRequest;
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
public class CallCenterController {

    private static final Logger log = LogManager.getLogger(CallCenterController.class);

    private final CallCenterService callCenterService;
    private final ActivityLogService logService;


    @PostMapping("/save/question")
    public ResponseEntity<WorkflowResponse> saveQuestion(
            Principal principal,
            @RequestBody QuestionRequest request,
            HttpServletRequest servletRequest) {

        WorkflowResponse response = callCenterService.saveQuestion(request);
        log.info("Question saved successfully");

        logService.logs(
                principal.getName(),
                "SAVE",
                "Questions saved successfully with question " + request.getQuestion(),
                "question",
                servletRequest.getRequestURI()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all/questions")
    public ResponseEntity<WorkflowResponse> getAllQuestions() {
        WorkflowResponse response = callCenterService.getAllQuestion();
        log.info("Fetched all questions successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/all/verification/status")
    public ResponseEntity<WorkflowResponse> getAllVerificationStatus() {
        WorkflowResponse response = callCenterService.getAllVerificationStatus();
        log.info("Fetched all verification status successfully");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/save/subactivity/questions")
    public ResponseEntity<WorkflowResponse> saveSubActivityQuestions(Principal principal, @RequestBody SubActivityQuestionsRequest request,HttpServletRequest servletRequest)
    {
        WorkflowResponse response = callCenterService.saveSubActivityQuestions(request);
        log.info("Questions mapped successfully to subActivityId={}", request.getSubActivityId());

        logService.logs(
                principal.getName(),
                "SAVE",
                "Questions mapped successfully to subActivity " + request.getSubActivityId(),
                "question",
                servletRequest.getRequestURI()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/questions/subactivity/id/{subActivityId}")
    public ResponseEntity<WorkflowResponse> getQuestionBySubActivityId(
            @PathVariable Long subActivityId) {

        WorkflowResponse response = callCenterService.getQuestionBySubActivityId(subActivityId);
        log.info("Fetched questions for subActivityId={}", subActivityId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save/callcenter/verification/data")
    public ResponseEntity<?> saveCallCenterVerification(Principal principal,@RequestBody CallCenterVerificationRequest request,HttpServletRequest servletRequest)
    {
        try {
            WorkflowResponse response =
                    callCenterService.saveCallCenterVerification(request);

            log.info("Call center verification updated for participantId={}, programId={}", request.getParticipantId(), request.getProgramId());

            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    String.format(
                            "Updated call center verification data for participant %s and program %s by %s",
                            request.getParticipantId(),
                            request.getProgramId(),
                            request.getVerifiedBy()),
                    "question",
                    servletRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);
        } catch (DataException ex) {
            log.error("DataException in saveCallCenterVerification(): {}", ex.getMessage(), ex);
            return RestControllerBase.error(ex);
        }
    }
    @GetMapping("/get/all/callcenter/verification/data")
    public ResponseEntity<WorkflowResponse> getAllCallCenterVerificationData() {

        WorkflowResponse response = callCenterService.getAllCallCenterVerificationData();
        log.info("Fetched all call center verification data successfully");

        return ResponseEntity.ok(response);
    }
}
