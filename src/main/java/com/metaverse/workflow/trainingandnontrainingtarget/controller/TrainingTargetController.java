package com.metaverse.workflow.trainingandnontrainingtarget.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TargetRequest;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TargetResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TrainingTargetService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/training-target")
public class TrainingTargetController {

    @Autowired
    private TrainingTargetService targetService;

    private final Logger log =  LogManager.getLogger(TrainingTargetController.class);

    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<?> getAllTrainingTargets(@PathVariable("agencyId") Long agencyId) {
        try {
            WorkflowResponse response = targetService.getTrainingTargetsByAgencyId(agencyId);
            log.info("getAllTrainingTargets | Agency ID: " + agencyId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveOrUpdate(@RequestBody TargetRequest request) {
        try {
            WorkflowResponse response = targetService.saveTrainingTarget(request);
            log.info("saveOrUpdate | Agency ID: " + request.getAgencyId());
            return ResponseEntity.ok(response);
        } catch (DataException ex) {
            return RestControllerBase.error(ex);
        }
    }

    @GetMapping("/agency-id/{agencyId}")
    public ResponseEntity<?> getAllQuaterTrainingTargets(@PathVariable("agencyId") Long agencyId) {
        try {
            WorkflowResponse response = targetService.getQuaterTrainingTargetsByAgencyId(agencyId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

}
