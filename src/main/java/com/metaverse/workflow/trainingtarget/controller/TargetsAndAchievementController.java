package com.metaverse.workflow.trainingtarget.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.trainingtarget.dtos.TargetsAndAchievementsResponseDto;
import com.metaverse.workflow.trainingtarget.service.TargetsAndAchievementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/targets-and-achievements")
@RequiredArgsConstructor
public class TargetsAndAchievementController {

    private final TargetsAndAchievementsService trainingAndAchievementsService;

    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<?> getAllTrainingTargets(@PathVariable("agencyId") Long agencyId) {
        List<TargetsAndAchievementsResponseDto> response = trainingAndAchievementsService.getTargetsAndAchievements(agencyId);
        return ResponseEntity.ok(response);
    }
}
