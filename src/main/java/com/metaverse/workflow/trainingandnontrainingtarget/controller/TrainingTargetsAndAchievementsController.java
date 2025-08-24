package com.metaverse.workflow.trainingandnontrainingtarget.controller;

import com.metaverse.workflow.trainingandnontrainingtarget.dtos.TrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TrainingTargetsAndAchievementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training/targets-and-achievements")
@RequiredArgsConstructor
public class TrainingTargetsAndAchievementsController {

    private final TrainingTargetsAndAchievementsService trainingAndAchievementsService;

    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<?> getAllTrainingTargets(@RequestParam String year, @PathVariable("agencyId") Long agencyId) {
        List<TrainingTargetsAndAchievementsResponse> response = trainingAndAchievementsService.getTargetsAndAchievements(year, agencyId);
        return ResponseEntity.ok(response);
    }
}
