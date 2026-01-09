package com.metaverse.workflow.aleap_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.VendorConnectionRequest;
import com.metaverse.workflow.aleap_handholding.service.VendorConnectionService;
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
@RequestMapping("/handholding-support/vendor-connection")
@Tag(name = "Handholding Support", description = "Vendor Connection APIs")
@RequiredArgsConstructor
public class VendorConnectionController {

    private static final Logger log = LogManager.getLogger(VendorConnectionController.class);
    private final VendorConnectionService service;
    private final ActivityLogService logService;

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestBody VendorConnectionRequest request) {

        try {
            WorkflowResponse response = service.save(request);
            log.info("Save successful for VendorConnection");

            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Vendor Connection saved successfully",
                    "VendorConnection",
                    "/vendor-connection/save"
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in save(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            Principal principal,
            @PathVariable Long id,
            @RequestBody VendorConnectionRequest request) {

        try {
            WorkflowResponse response = service.update(id, request);
            log.info("Update successful for id={}", id);

            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "Vendor Connection updated successfully",
                    "VendorConnection",
                    "/vendor-connection/update/" + id
            );

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

    @GetMapping("/sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivityId(@PathVariable Long subActivityId) {
        WorkflowResponse response = service.getByNonTrainingSubActivityId(subActivityId);
        log.info("getBySubActivityId successful for subActivityId={}", subActivityId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            Principal principal,
            @PathVariable Long id) {

        try {
            WorkflowResponse response = service.delete(id);
            log.info("Delete successful for id={}", id);

            logService.logs(
                    principal.getName(),
                    "DELETE",
                    "Vendor Connection deleted successfully",
                    "VendorConnection",
                    "/vendor-connection/delete/" + id
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in delete(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }
}
