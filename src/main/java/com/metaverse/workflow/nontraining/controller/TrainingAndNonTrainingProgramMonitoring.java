package com.metaverse.workflow.nontraining.controller;

import com.metaverse.workflow.nontraining.service.ProgramMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/training-non-training/monitoring")
public class TrainingAndNonTrainingProgramMonitoring {

    @Autowired
    private ProgramMonitoringService programMonitoringService;

    @GetMapping(path = "/program/monitor")
    public ResponseEntity<?> getAgencyProgramMonitor(Long agencyId){
        return ResponseEntity.ok(programMonitoringService.getAllTrainingAndNonTrainings(agencyId));
    }
}
