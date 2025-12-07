package com.metaverse.workflow.trainingandnontrainingtarget.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.programoutcometargets.service.PhysicalTargetRequest;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.NonTrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.service.NonTrainingTargetsAndAchievementsService;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TargetRequest;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TargetResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/non-training")
@RequiredArgsConstructor
public class NonTrainingTargetController {

    private final NonTrainingTargetsAndAchievementsService nonTrainingTargetsAndAchievementsService;
    private final ActivityLogService logService;

    @GetMapping("/targets-and-achievements/agency/{agencyId}")
    public ResponseEntity<?> getAllTrainingTargets(@RequestParam String year, @PathVariable("agencyId") Long agencyId) {
        List<NonTrainingTargetsAndAchievementsResponse> response = nonTrainingTargetsAndAchievementsService.getTargetsAndAchievements(year, agencyId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/target/save")
    public ResponseEntity<?> createPhysicalTarget(@RequestBody TargetRequest request, Principal principal,
                                                  HttpServletRequest servletRequest) {
        try {
            WorkflowResponse response = nonTrainingTargetsAndAchievementsService.saveNonTrainingTarget(request);
            logService.logs(principal.getName(), "SAVE",
                    "NonTraining Target created successfully | Agency ID: " + request.getAgencyId(),
                    "NonTrainingTarget",
                    servletRequest.getRequestURI());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
    @GetMapping("targets/by-agency")
    public ResponseEntity<List<TargetResponse>> getNonTrainingTargets(@RequestParam String year, @RequestParam Long agencyId) {
        List<TargetResponse> response = nonTrainingTargetsAndAchievementsService.getNonTrainingTargets(year, agencyId);
        return ResponseEntity.ok(response);
    }
}
