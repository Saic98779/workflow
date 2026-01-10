package com.metaverse.workflow.aleap_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.GovtSchemeApplicationRequest;
import com.metaverse.workflow.aleap_handholding.service.GovtSchemeApplicationService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/handholding-support/govt-scheme-application")
@Tag(name = "Handholding Support", description = "Govt Scheme Application APIs")
@RequiredArgsConstructor
public class GovtSchemeApplicationController {

    private final GovtSchemeApplicationService service;
    private final ActivityLogService logService;
    private static final Logger log = LogManager.getLogger(GovtSchemeApplicationController.class);

    @PostMapping("/save")
    public ResponseEntity<?> save(Principal principal, @RequestBody GovtSchemeApplicationRequest request) {
        try {
            WorkflowResponse response = service.save(request);

            log.info("Save successful for GovtSchemeApplication");
            logService.logs(principal.getName(), "SAVE", "Govt Scheme Application saved", "GovtSchemeApplication", "/govt-scheme-application/save");

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in save(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(Principal principal, @PathVariable Long id, @RequestBody GovtSchemeApplicationRequest request) {
        try {
            WorkflowResponse response = service.update(id, request);

            log.info("Update successful for id={}", id);
            logService.logs(principal.getName(), "UPDATE", "Govt Scheme Application updated", "GovtSchemeApplication", "/govt-scheme-application/update/" + id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in update(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            WorkflowResponse response = service.getById(id);
            log.info("getById successful for id={}", id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in getById(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(Principal principal, @PathVariable Long id) {
        try {
            WorkflowResponse response = service.delete(id);
            log.info("Delete successful for id={}", id);
            logService.logs(principal.getName(), "DELETE", "Govt Scheme Application deleted", "GovtSchemeApplication", "/govt-scheme-application/delete/" + id);

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in delete(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivityId(@PathVariable Long subActivityId) {
        WorkflowResponse response = service.getBySubActivityId(subActivityId);
        log.info("getBySubActivityId successful for subActivityId={}", subActivityId);
        return ResponseEntity.ok(response);
    }
}
