package com.metaverse.workflow.aleap_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.BankNbfcFinanceRequest;
import com.metaverse.workflow.aleap_handholding.service.BankNbfcFinanceService;
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
@RequestMapping("/handholding-support/bank-nbfc-finance")
@Tag(name = "Handholding Support", description = "Bank/NBFC & Design Studio APIs")
@RequiredArgsConstructor
public class BankNbfcFinanceController {

    private final BankNbfcFinanceService service;
    private final ActivityLogService logService;
    private static final Logger log = LogManager.getLogger(BankNbfcFinanceController.class);

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestBody BankNbfcFinanceRequest request) {

        try {
            WorkflowResponse response = service.save(request);

            log.info("Save successful for BankNbfcFinance");
            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Bank / NBFC Finance created successfully",
                    "BankNbfcFinance",
                    "/bank-nbfc-finance/save"
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            log.error("DataException in save(): {}", e.getMessage());
            return RestControllerBase.error(e);
        }
    }

    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            Principal principal,
            @PathVariable Long id,
            @RequestBody BankNbfcFinanceRequest request) {

        try {
            WorkflowResponse response = service.update(id, request);

            log.info("Update successful for id={}", id);
            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "Bank / NBFC Finance updated successfully",
                    "BankNbfcFinance",
                    "/bank-nbfc-finance/update/" + id
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
                    "Bank / NBFC Finance viewed",
                    "BankNbfcFinance",
                    "/bank-nbfc-finance/get/" + id
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

        WorkflowResponse response = service.getByNonTrainingSubActivityId(subActivityId);

        log.info("getBySubActivity successful for subActivityId={}", subActivityId);
        logService.logs(
                principal.getName(),
                "VIEW",
                "Bank / NBFC Finance viewed by sub activity",
                "BankNbfcFinance",
                "/bank-nbfc-finance/get-by-sub-activity/" + subActivityId
        );

        return ResponseEntity.ok(response);
    }
}
