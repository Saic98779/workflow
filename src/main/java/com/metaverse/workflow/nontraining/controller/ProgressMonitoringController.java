package com.metaverse.workflow.nontraining.controller;

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

    @GetMapping
    public ResponseEntity<?> getAgencyProgramMonitor(Long agencyId){
        return ResponseEntity.ok(progressMonitoringService.getAllTrainingAndNonTrainings(agencyId));
    }
}
