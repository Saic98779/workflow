package com.metaverse.workflow.trainingandnontrainingtarget.controller;

import com.metaverse.workflow.trainingandnontrainingtarget.dtos.NonTrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.TrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.service.NonTrainingTargetsAndAchievementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/non-training/targets-and-achievements")
@RequiredArgsConstructor
public class NonTrainingTargetController {

    private final NonTrainingTargetsAndAchievementsService nonTrainingTargetsAndAchievementsService;

    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<?> getAllTrainingTargets(@RequestParam String year, @PathVariable("agencyId") Long agencyId) {
        List<NonTrainingTargetsAndAchievementsResponse> response = nonTrainingTargetsAndAchievementsService.getTargetsAndAchievements(year, agencyId);
        return ResponseEntity.ok(response);
    }
}
