package com.metaverse.workflow.trainingandnontrainingtarget.controller;

import com.metaverse.workflow.trainingandnontrainingtarget.dtos.TrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TargetResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TrainingTargetsAndAchievementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingTargetsAndAchievementsController {

    private final TrainingTargetsAndAchievementsService trainingAndAchievementsService;

    @GetMapping("/targets-and-achievements/agency/{agencyId}")
    public ResponseEntity<?> getAllTrainingTargets(@RequestParam String year, @PathVariable("agencyId") Long agencyId) {
        List<TrainingTargetsAndAchievementsResponse> response = trainingAndAchievementsService.getTargetsAndAchievements(year, agencyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/targets/by-agency")
    public ResponseEntity<?> getAllTrainingTargetsByAgencyId(@RequestParam String year, @RequestParam Long agencyId) {
        List<TargetResponse> response = trainingAndAchievementsService.getTrainingTargets(year, agencyId);
        return ResponseEntity.ok(response);
    }
}
