package com.metaverse.workflow.nontraining.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.nontraining.dto.NonTrainingActivityDto;
import com.metaverse.workflow.nontraining.dto.PhysicalFinancialDto;
import com.metaverse.workflow.nontraining.service.NonTrainingAchievementService;
import com.metaverse.workflow.nontraining.service.NonTrainingActivityService;
import com.metaverse.workflow.nontraining.service.ProgressMonitoringService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/progress/monitoring")
public class ProgressMonitoringController {

    @Autowired
    private ProgressMonitoringService progressMonitoringService;

    @Autowired
    private NonTrainingAchievementService nonTrainingAchievementService;

    @Autowired
    private NonTrainingActivityService nonTrainingActivityService;

    @GetMapping("/aleap")
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
        return ResponseEntity.status(400).body(WorkflowResponse.builder().data(null)
                .message("FAILURE")
                .status(400)
                .build());
    }

    @PutMapping("/non-training-achievement/{nonTrainingAchievementId}")
    public ResponseEntity<?> updateNonTrainingAchievement(@PathVariable Long nonTrainingAchievementId, @RequestBody PhysicalFinancialDto request) {
        try {
            Optional<NonTrainingAchievement> updated = nonTrainingAchievementService.updateNonTrainingAchievement(nonTrainingAchievementId,request);

            if (updated.isPresent()) {
                return ResponseEntity.ok(
                        WorkflowResponse.builder()
                                .data("")
                                .message("Updated Successfully")
                                .status(200)
                                .build()
                );
            } else {
                return ResponseEntity.badRequest()
                        .body(new WorkflowResponse(400, "ID is missing in request", "", 0, 0));
            }
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new WorkflowResponse(404, ex.getMessage(), "", 0, 0));
        }
    }

    @GetMapping(path = "get/activities/{agencyId}")
    public ResponseEntity<?> getAllActivitiesByAgency(@PathVariable Long agencyId){
        List<NonTrainingActivityDto> allActivitiesByAgency = nonTrainingActivityService.getAllActivitiesByAgency(agencyId);
        if (allActivitiesByAgency ==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new WorkflowResponse(404, "Activity not present", "", 0, 0));
        }
        return ResponseEntity.ok(allActivitiesByAgency);
    }
}