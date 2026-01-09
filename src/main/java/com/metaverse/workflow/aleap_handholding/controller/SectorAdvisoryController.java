package com.metaverse.workflow.aleap_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.SectorAdvisoryRequest;
import com.metaverse.workflow.aleap_handholding.service.SectorAdvisoryService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/handholding-support/sector-advisory")
@Tag(name = "Handholding Support", description = "Sector Advisory APIs")
@RequiredArgsConstructor
public class SectorAdvisoryController {

    private final SectorAdvisoryService service;
    private final ActivityLogService logService;
    private static final Logger log = LogManager.getLogger(SectorAdvisoryController.class);

    /* ========================= SAVE ========================= */

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(Principal principal, @RequestBody SectorAdvisoryRequest request) {
        try {
            WorkflowResponse response = service.save(request);
            log.info("Save successful for SectorAdvisory");
            logService.logs(principal.getName(), "SAVE", "Sector advisory saved successfully", "SectorAdvisory", "/sector-advisory/save");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in save(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    /* ========================= UPDATE ========================= */

    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(Principal principal, @PathVariable Long id, @RequestBody SectorAdvisoryRequest request) {
        try {
            WorkflowResponse response = service.update(id, request);
            log.info("Update successful for id={}", id);
            logService.logs(principal.getName(), "UPDATE", "Sector advisory updated successfully", "SectorAdvisory", "/sector-advisory/update/" + id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in update(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        } finally {
            log.info("Exiting update()");
        }
    }

    /* ========================= GET BY ID ========================= */

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(
            Principal principal,
            @PathVariable Long id) {

        try {
            WorkflowResponse response = service.getById(id);
            log.info("getById successful for id={}", id);
            logService.logs(principal.getName(), "VIEW", "Sector advisory viewed", "SectorAdvisory", "/sector-advisory/get/" + id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in getById(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    /* ================= GET BY NON TRAINING SUB ACTIVITY ================= */

    @GetMapping("/get-by-sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivity(Principal principal, @PathVariable Long subActivityId) {
        WorkflowResponse response = service.getByNonTrainingSubActivityId(subActivityId);
        log.info("getBySubActivity successful for subActivityId={}, response={}", subActivityId, response);
        logService.logs(principal.getName(), "VIEW", "Sector advisory viewed by sub activity", "SectorAdvisory", "/sector-advisory/get-by-sub-activity/" + subActivityId);
        return ResponseEntity.ok(response);
    }

    /* ========================= DELETE ========================= */

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(Principal principal, @PathVariable Long id) {
        try {
            WorkflowResponse response = service.delete(id);
            log.info("Delete successful for id={}", id);
            logService.logs(principal.getName(), "DELETE", "Sector advisory deleted successfully", "SectorAdvisory", "/sector-advisory/delete/" + id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in delete(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }
}
