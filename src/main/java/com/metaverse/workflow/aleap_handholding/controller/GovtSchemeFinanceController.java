package com.metaverse.workflow.aleap_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.GovtSchemeFinanceRequest;
import com.metaverse.workflow.aleap_handholding.service.GovtSchemeFinanceService;
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
@RequestMapping("/handholding-support/govt-scheme-finance")
@Tag(name = "Handholding Support", description = "Govt Scheme Finance APIs")
@RequiredArgsConstructor
public class GovtSchemeFinanceController {

    private final GovtSchemeFinanceService service;
    private final ActivityLogService logService;
    private static final Logger log = LogManager.getLogger(GovtSchemeFinanceController.class);

    /* ========================= SAVE ========================= */

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestBody GovtSchemeFinanceRequest request) {

        try {
            WorkflowResponse response = service.save(request);

            log.info("Save successful for GovtSchemeFinance");
            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Govt Scheme Finance saved successfully",
                    "GovtSchemeFinance",
                    "/govt-scheme-finance/save"
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            log.error("DataException in save(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    /* ========================= UPDATE ========================= */

    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            Principal principal,
            @PathVariable Long id,
            @RequestBody GovtSchemeFinanceRequest request) {

        try {
            WorkflowResponse response = service.update(id, request);

            log.info("Update successful for id={}", id);
            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "Govt Scheme Finance updated successfully",
                    "GovtSchemeFinance",
                    "/govt-scheme-finance/update/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            log.error("DataException in update(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
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
            logService.logs(
                    principal.getName(),
                    "VIEW",
                    "Govt Scheme Finance viewed",
                    "GovtSchemeFinance",
                    "/govt-scheme-finance/get/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            log.error("DataException in getById(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    /* ========================= GET BY SUB ACTIVITY ========================= */

    @GetMapping("/get-by-sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivity(
            Principal principal,
            @PathVariable Long subActivityId) {

        WorkflowResponse response =
                service.getByNonTrainingSubActivityId(subActivityId);

        log.info("getBySubActivity successful for subActivityId={}", subActivityId);
        logService.logs(
                principal.getName(),
                "VIEW",
                "Govt Scheme Finance viewed by sub activity",
                "GovtSchemeFinance",
                "/govt-scheme-finance/get-by-sub-activity/" + subActivityId
        );

        return ResponseEntity.ok(response);
    }

    /* ========================= DELETE ========================= */

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            Principal principal,
            @PathVariable Long id) {

        try {
            WorkflowResponse response = service.delete(id);

            log.info("Delete successful for id={}", id );
            logService.logs(
                    principal.getName(),
                    "DELETE",
                    "Govt Scheme Finance deleted successfully",
                    "GovtSchemeFinance",
                    "/govt-scheme-finance/delete/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            log.error("DataException in delete(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }
}
