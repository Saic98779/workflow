package com.metaverse.workflow.ESDPTraining.controller;

import com.metaverse.workflow.ESDPTraining.service.ESDPTrainingRequest;
import com.metaverse.workflow.ESDPTraining.service.ESDPTrainingService;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
public class ESDPTrainingController {
    @Autowired
    private ESDPTrainingService esdpTrainingService;
    @Autowired
    private ActivityLogService logService;

    @PostMapping("/SaveESDPTraining")
    public ResponseEntity<WorkflowResponse> SaveESDPTraining(Principal principal, @RequestBody ESDPTrainingRequest esdpTrainingRequest)
    {
        WorkflowResponse response=  esdpTrainingService.saveESDPTrainingProgram(esdpTrainingRequest);
        logService.logs(principal.getName(), "SAVE","ESDPTraining details saved successfully","Counsellor","/SaveESDPTraining");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getESDPTraining")
    public ResponseEntity<WorkflowResponse> getESDPTrainingData()
    {
        WorkflowResponse response= esdpTrainingService.getESDPTrainingProgramData();
        return ResponseEntity.ok(response);
    }
}
