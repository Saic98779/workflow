package com.metaverse.workflow.nontraining.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.nontraining.dto.NonTrainingActivityDto;
import com.metaverse.workflow.nontraining.dto.PhysicalFinancialDto;
import com.metaverse.workflow.nontraining.dto.TrainingProgramDto;
import com.metaverse.workflow.nontraining.service.NonTrainingAchievementService;
import com.metaverse.workflow.nontraining.service.NonTrainingActivityService;
import com.metaverse.workflow.nontraining.service.ProgressMonitoringService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    private static final Logger log = LogManager.getLogger(ProgressMonitoringController.class);

    @Autowired
    private ActivityLogService logService;

    @GetMapping("/non-training-targets")
    public ResponseEntity<?> getAgencyProgramMonitor(Long agencyId){
        return ResponseEntity.ok(progressMonitoringService.nonTrainingProgressMonitoring(agencyId));
    }


    @GetMapping("/training-targets")
    public ResponseEntity<?> getProgramMonitor(Long agencyId){
        List<TrainingProgramDto> allTrainingProgressMonitoringProgress = progressMonitoringService.getAllTrainingProgressMonitoringProgress(agencyId);
        return ResponseEntity.ok(allTrainingProgressMonitoringProgress);
    }
    /**
     * Fetch Activity-wise training progress data for a given agency.
     *
     * Example: GET /api/training-progress/activity-wise?agencyId=5
     */
    @GetMapping("/activity-wise")
    public ResponseEntity<WorkflowResponse> getActivityWiseTrainingProgress(
            @RequestParam("agencyId") Long agencyId) {

        List<TrainingProgramDto> progressList = progressMonitoringService.getAllTrainingProgress(agencyId);

        WorkflowResponse response = WorkflowResponse.builder()
                .message("Activity-wise training progress fetched successfully")
                .data(progressList)
                .status(200)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "non-training/physical/financial/{subActivityId}")
    public ResponseEntity<?> getPhysicalFinancial(@PathVariable Long subActivityId){
        PhysicalFinancialDto physicalFinancial = nonTrainingAchievementService.getPhysicalFinancial(subActivityId);
        if(physicalFinancial != null){
            return ResponseEntity.ofNullable(WorkflowResponse.builder().data(physicalFinancial)
                    .message("SUCCESS")
                    .status(200)
                    .build());
        }
        return ResponseEntity.ofNullable(WorkflowResponse.builder().data(null)
                .message("Data not Exist for this sub activity ")
                .status(400)
                .build());
    }

    @PutMapping("/non-training-achievement/{nonTrainingAchievementId}")
    public ResponseEntity<?> updateNonTrainingAchievement(@PathVariable Long nonTrainingAchievementId, @RequestBody PhysicalFinancialDto request, Principal principal) {
        try {
            Optional<NonTrainingAchievement> updated = nonTrainingAchievementService.updateNonTrainingAchievement(nonTrainingAchievementId,request);

            if (updated.isPresent()) {
                logService.logs(principal.getName(), "UPDATE",
                        String.format("Non-Training Achievement updated | ID: %s | Request: %s",
                                nonTrainingAchievementId, request.toString()),
                        "NON_TRAINING",
                        "/non-training-achievement/{nonTrainingAchievementId}"
                );
                log.info("Non-Training Achievement updated Successfully");
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