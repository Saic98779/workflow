package com.metaverse.workflow.aleap_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.LoanDocumentPreparationRequest;
import com.metaverse.workflow.aleap_handholding.service.LoanDocumentPreparationService;
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
@RequestMapping("/handholding-support/loan-document-preparation")
@Tag(name = "Handholding Support", description = "Loan Document Preparation APIs")
@RequiredArgsConstructor
public class LoanDocumentPreparationController {

    private final LoanDocumentPreparationService service;
    private final ActivityLogService logService;
    private static final Logger log = LogManager.getLogger(LoanDocumentPreparationController.class);

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestBody LoanDocumentPreparationRequest request) {

        try {
            WorkflowResponse response = service.save(request);

            log.info("Save successful for LoanDocumentPreparation");
            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Loan Document Preparation saved successfully",
                    "LoanDocumentPreparation",
                    "/loan-document-preparation/save"
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
            @RequestBody LoanDocumentPreparationRequest request) {

        try {
            WorkflowResponse response = service.update(id, request);

            log.info("Update successful for id={}", id);
            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "Loan Document Preparation updated successfully",
                    "LoanDocumentPreparation",
                    "/loan-document-preparation/update/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            log.error("DataException in update(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

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
                    "Loan Document Preparation viewed",
                    "LoanDocumentPreparation",
                    "/loan-document-preparation/get/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            log.error("DataException in getById(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

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
                "Loan Document Preparation viewed by sub activity",
                "LoanDocumentPreparation",
                "/loan-document-preparation/get-by-sub-activity/" + subActivityId
        );

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
                    "Loan Document Preparation deleted successfully",
                    "LoanDocumentPreparation",
                    "/loan-document-preparation/delete/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            log.error("DataException in delete(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }
}
