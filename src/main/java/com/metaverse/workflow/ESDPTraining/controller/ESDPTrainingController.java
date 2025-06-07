package com.metaverse.workflow.ESDPTraining.controller;

import com.metaverse.workflow.ESDPTraining.service.ESDPTrainingRequest;
import com.metaverse.workflow.ESDPTraining.service.ESDPTrainingService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ESDPTrainingController {
    @Autowired
    ESDPTrainingService esdpTrainingService;

    @PostMapping("/SaveESDPTraining")
    public ResponseEntity<WorkflowResponse> SaveESDPTraining(@RequestBody ESDPTrainingRequest esdpTrainingRequest)
    {
        WorkflowResponse response=  esdpTrainingService.saveESDPTrainingProgram(esdpTrainingRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getESDPTraining")
    public ResponseEntity<WorkflowResponse> getESDPTrainingData()
    {
        WorkflowResponse response= esdpTrainingService.getESDPTrainingProgramData();
        return ResponseEntity.ok(response);
    }
}
