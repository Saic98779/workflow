package com.metaverse.workflow.aleap_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.BankNbfcFinanceRequest;
import com.metaverse.workflow.aleap_handholding.service.BankNbfcFinanceService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/bank-nbfc-finance")
@RequiredArgsConstructor
public class BankNbfcFinanceController {

    private final BankNbfcFinanceService service;
    private final ActivityLogService logService;

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestBody BankNbfcFinanceRequest request) {

        try {
            WorkflowResponse response = service.save(request);

            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Bank / NBFC Finance created successfully",
                    "BankNbfcFinance",
                    "/bank-nbfc-finance/save"
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
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

            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "Bank / NBFC Finance updated successfully",
                    "BankNbfcFinance",
                    "/bank-nbfc-finance/update/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(
            Principal principal,
            @PathVariable Long id) {

        try {
            WorkflowResponse response = service.getById(id);

            logService.logs(
                    principal.getName(),
                    "VIEW",
                    "Bank / NBFC Finance viewed",
                    "BankNbfcFinance",
                    "/bank-nbfc-finance/get/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/get-by-sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivity(
            Principal principal,
            @PathVariable Long subActivityId) {

        WorkflowResponse response = service.getByNonTrainingSubActivityId(subActivityId);

        logService.logs(
                principal.getName(),
                "VIEW",
                "Bank / NBFC Finance viewed by sub activity",
                "BankNbfcFinance",
                "/bank-nbfc-finance/get-by-sub-activity/" + subActivityId
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            Principal principal,
            @PathVariable Long id) {

        try {
            WorkflowResponse response = service.delete(id);

            logService.logs(
                    principal.getName(),
                    "DELETE",
                    "Bank / NBFC Finance deleted successfully",
                    "BankNbfcFinance",
                    "/bank-nbfc-finance/delete/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
