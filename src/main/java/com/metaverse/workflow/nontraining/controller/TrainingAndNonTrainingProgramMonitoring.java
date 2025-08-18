package com.metaverse.workflow.nontraining.controller;

import com.metaverse.workflow.nontraining.service.ProgramMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/TrainingAndNonTrainingProgram")
public class TrainingAndNonTrainingProgramMonitoring {

    @Autowired
    private ProgramMonitoringService programMonitoringService;

    @GetMapping(path = "/program/monitor/{agencyId}")
    public ResponseEntity<?> getAgencyProgramMonitor(@PathVariable Long agencyId) {
        try {
            var result = programMonitoringService.getAllTrainingAndNonTrainings(agencyId);

            if (result == null)
                return ResponseEntity.noContent().build();
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid agencyId: " + agencyId);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }
}