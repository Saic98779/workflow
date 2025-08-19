package com.metaverse.workflow.trainingtarget.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.trainingtarget.service.TrainingTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/training_target")
public class TrainingTargetController {

    @Autowired
    private TrainingTargetService targetService;

    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<?> getAllTrainingTargets(@PathVariable("agencyId") Long agencyId) {
        try {
            WorkflowResponse response = targetService.getTrainingTargetsByAgencyId(agencyId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }

    }

}
