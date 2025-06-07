package com.metaverse.workflow.callcenter.controller;

import com.metaverse.workflow.callcenter.service.CallCenterService;
import com.metaverse.workflow.callcenter.service.CallCenterVerificationRequest;
import com.metaverse.workflow.callcenter.service.QuestionRequest;
import com.metaverse.workflow.callcenter.service.SubActivityQuestionsRequest;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CallCenterController {
    @Autowired
    CallCenterService callCenterService;

    @PostMapping("/save/question")
    public ResponseEntity<WorkflowResponse> saveQuestion(@RequestBody QuestionRequest request)
    {
        WorkflowResponse response=callCenterService.saveQuestion(request);
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
    public ResponseEntity<WorkflowResponse> saveSubActivityQuestions(@RequestBody SubActivityQuestionsRequest request)
    {
        WorkflowResponse response = callCenterService.saveSubActivityQuestions(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/questions/subactivity/id/{subActivityId}")
    public ResponseEntity<WorkflowResponse> getQuestionBySubActivityId(@PathVariable Long subActivityId)
    {
        WorkflowResponse response = callCenterService.getQuestionBySubActivityId(subActivityId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save/callcenter/verification/data")
    public ResponseEntity<?> saveCallCenterVerification(@RequestBody CallCenterVerificationRequest request)
    {
        try {
            WorkflowResponse response = callCenterService.saveCallCenterVerification(request);
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
