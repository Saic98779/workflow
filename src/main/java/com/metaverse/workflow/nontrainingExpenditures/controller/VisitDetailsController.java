package com.metaverse.workflow.nontrainingExpenditures.controller;


import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.service.VisitDetailsRequest;
import com.metaverse.workflow.nontrainingExpenditures.service.VisitDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/visit-details")
@Tag(name = "Non Training", description = "Visit Details APIs")
@RequiredArgsConstructor
public class VisitDetailsController {

    private final VisitDetailsService service;
    private final ActivityLogService logService;
    private final static Logger log =  LogManager.getLogger(VisitDetailsController.class);

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody VisitDetailsRequest request, Principal principal) {
        try {
            WorkflowResponse response = service.save(request);
            logService.logs(principal.getName(), "SAVE", "Visit Details saved successfully",
                    "VisitDetails", "/visit-details/save");
            log.info("Visit Details saved successfully");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody VisitDetailsRequest request,
                                    Principal principal) {
        try {
            WorkflowResponse response = service.update(id, request);
            logService.logs(principal.getName(), "UPDATE", "Visit Details updated successfully",
                    "VisitDetails", "/visit-details/update/" + id);
            log.info("Visit Details updated successfully");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Principal principal) {
        try {
            WorkflowResponse response = service.delete(id);
            logService.logs(principal.getName(), "DELETE", "Visit Details deleted successfully",
                    "VisitDetails", "/visit-details/delete/" + id);
            log.info("Visit Details deleted successfully");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            WorkflowResponse response = service.getVisitDetailsById(id);
            log.info("Visit Details found successfully");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/get-by-sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivityId(@PathVariable Long subActivityId) {
            WorkflowResponse response = service.getBySubActivityId(subActivityId);
            return ResponseEntity.ok(response);

    }
}


