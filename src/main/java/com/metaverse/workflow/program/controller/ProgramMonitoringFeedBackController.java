package com.metaverse.workflow.program.controller;

import com.metaverse.workflow.program.repository.ProgramMonitoringFeedBackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("program-monitoring")
public class ProgramMonitoringFeedBackController {

    @Autowired
    private ProgramMonitoringFeedBackRepository feedbackRepository;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProgramMonitoringFeedback(@PathVariable Long id) {
        return feedbackRepository.findById(id).map(feedback -> {
            feedbackRepository.delete(feedback);
            return ResponseEntity.ok("Program Monitoring FeedBack deleted successfully.");
        }).orElse(ResponseEntity.notFound().build());
    }
}

