package com.metaverse.workflow.nontraining.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.nontraining.dto.PhysicalFinancialDto;
import com.metaverse.workflow.nontraining.service.NonTrainingAchievementService;
import com.metaverse.workflow.nontraining.service.ProgressMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/progress/monitoring")
public class ProgressMonitoringController {

    @Autowired
    private ProgressMonitoringService progressMonitoringService;

    @Autowired
    private NonTrainingAchievementService nonTrainingAchievementService;

    @GetMapping
    public ResponseEntity<?> getAgencyProgramMonitor(Long agencyId){
        return ResponseEntity.ok(progressMonitoringService.getAllTrainingAndNonTrainings(agencyId));
    }

        @GetMapping(path = "non-training/physical/financial/{activityId}")
    public ResponseEntity<?> getPhysicalFinancial(Long activityId){
        PhysicalFinancialDto physicalFinancial = nonTrainingAchievementService.getPhysicalFinancial(activityId);
        if(physicalFinancial != null){
            return ResponseEntity.ofNullable(WorkflowResponse.builder().data(physicalFinancial)
                    .message("SUCCESS")
                    .status(200)
                    .build());
        }
        return ResponseEntity.status(400).body(WorkflowResponse.builder().data(physicalFinancial)
                .message("FAILURE")
                .status(400)
                .build());
    }

}
