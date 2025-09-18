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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class CallCenterController {
    @Autowired
    CallCenterService callCenterService;

    @Autowired
    ActivityLogService logService;

    @PostMapping("/save/question")
    public ResponseEntity<WorkflowResponse> saveQuestion(Principal principal, @RequestBody QuestionRequest request, HttpServletRequest servletRequest)
    {
        WorkflowResponse response=callCenterService.saveQuestion(request);
        logService.logs(principal.getName(), "SAVE", "Questions saved successfully with question " + request.getQuestion(),"question", servletRequest.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all/questions")
    public ResponseEntity<WorkflowResponse> getAllQuestions()
    {
        WorkflowResponse response = callCenterService.getAllQuestion();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/all/verification/status")
    public ResponseEntity<WorkflowResponse> getAllVerificationStatus()
    {
        WorkflowResponse response = callCenterService.getAllVerificationStatus();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/save/subactivity/questions")
    public ResponseEntity<WorkflowResponse> saveSubActivityQuestions(Principal principal, @RequestBody SubActivityQuestionsRequest request,HttpServletRequest servletRequest)
    {
        WorkflowResponse response = callCenterService.saveSubActivityQuestions(request);
        logService.logs(principal.getName(), "SAVE", "Questions mapped successfully to subActivity " + request.getSubActivityId(),"question", servletRequest.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/questions/subactivity/id/{subActivityId}")
    public ResponseEntity<WorkflowResponse> getQuestionBySubActivityId(@PathVariable Long subActivityId)
    {
        WorkflowResponse response = callCenterService.getQuestionBySubActivityId(subActivityId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save/callcenter/verification/data")
    public ResponseEntity<?> saveCallCenterVerification(Principal principal,@RequestBody CallCenterVerificationRequest request,HttpServletRequest servletRequest)
    {
        try {
            WorkflowResponse response = callCenterService.saveCallCenterVerification(request);
            logService.logs(principal.getName(), "UPDATE", String.format("Updated call center verification data for participant %s and program %s by %s" , request.getParticipantId() , request.getProgramId(), request.getVerifiedBy()),"question", servletRequest.getRequestURI());
            return ResponseEntity.ok(response);
        }
        catch (DataException ex) {
            return RestControllerBase.error(ex);
        }
    }
    @GetMapping("/get/all/callcenter/verification/data")
    public ResponseEntity<WorkflowResponse> getAllCallCenterVerificationData()
    {
        WorkflowResponse response =  callCenterService.getAllCallCenterVerificationData();
        return ResponseEntity.ok(response);
    }
}
