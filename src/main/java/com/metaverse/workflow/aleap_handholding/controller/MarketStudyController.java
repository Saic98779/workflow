package com.metaverse.workflow.aleap_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.FeasibilityInputRequest;
import com.metaverse.workflow.aleap_handholding.request_dto.MarketStudyRequest;
import com.metaverse.workflow.aleap_handholding.service.MarketStudyService;
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
@RequestMapping("/handholding-support/market-study")
@Tag(name = "Handholding Support", description = "Market Study APIs")
@RequiredArgsConstructor
public class MarketStudyController {

    private final MarketStudyService service;
    private final ActivityLogService logService;
    private static final Logger log = LogManager.getLogger(MarketStudyController.class);

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestBody MarketStudyRequest request) {

        try {
            WorkflowResponse response = service.save(request);
            log.info("Save successful for MarketStudy, ");
            logService.logs(principal.getName(), "SAVE", "Market Study saved successfully", "MarketStudy", "/market-study/save");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in save(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(Principal principal, @PathVariable Long id, @RequestBody MarketStudyRequest request) {
        try {
            WorkflowResponse response = service.update(id, request);
            log.info("Update successful for id={}", id);
            logService.logs(principal.getName(), "UPDATE", "Market Study updated successfully", "MarketStudy", "/market-study/update/" + id);
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
            log.info("getById successful for id={}, response={}", id, response);
            logService.logs(principal.getName(), "VIEW", "Market Study viewed", "MarketStudy", "/market-study/get/" + id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in getById(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/get-by-sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivity(Principal principal, @PathVariable Long subActivityId) {
        WorkflowResponse response = service.getByNonTrainingSubActivityId(subActivityId);
        log.info("getBySubActivity successful for subActivityId={}", subActivityId);
        logService.logs(principal.getName(), "VIEW", "Market Study viewed by sub activity", "MarketStudy", "/market-study/get-by-sub-activity/" + subActivityId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(Principal principal, @PathVariable Long id) {
        try {
            WorkflowResponse response = service.delete(id);
            log.info("Delete successful for id={}", id);
            logService.logs(principal.getName(), "DELETE", "Market Study deleted successfully", "MarketStudy", "/market-study/delete/" + id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in delete(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @PostMapping(path = "/feasibility-input/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveFeasibilityInput(Principal principal, @RequestBody FeasibilityInputRequest request) {
        try {
            WorkflowResponse response = service.saveFeasibilityInput(request);
            log.info("SaveFeasibilityInput successful, response={}", response);
            logService.logs(principal.getName(), "SAVE", "Feasibility input saved successfully", "FeasibilityInput", "/market-study/feasibility-input/save");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in saveFeasibilityInput(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @PutMapping(path = "/feasibility-input/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateFeasibilityInput(Principal principal, @PathVariable Long id, @RequestBody FeasibilityInputRequest request) {
        try {
            WorkflowResponse response = service.updateFeasibilityInput(id, request);
            log.info("UpdateFeasibilityInput successful for id={}", id);
            logService.logs(principal.getName(), "UPDATE", "Feasibility input updated successfully", "FeasibilityInput", "/market-study/feasibility-input/update/" + id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in updateFeasibilityInput(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/feasibility-input/get/{id}")
    public ResponseEntity<?> getFeasibilityInputById(Principal principal, @PathVariable Long id) {
        try {
            WorkflowResponse response = service.getByFeasibilityInputId(id);
            log.info("getFeasibilityInputById successful for id={}", id);
            logService.logs(principal.getName(), "VIEW", "Feasibility input viewed", "FeasibilityInput", "/market-study/feasibility-input/get/" + id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in getFeasibilityInputById(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/feasibility-input/get-by-sub-activity/{subActivityId}")
    public ResponseEntity<?> getFeasibilityInputBySubActivity(Principal principal,
                                                              @PathVariable Long subActivityId) {
        WorkflowResponse response = service.getByFeasibilityInputSubActivityId(subActivityId);
        log.info("getFeasibilityInputBySubActivity successful for subActivityId={}", subActivityId);
        logService.logs(principal.getName(), "VIEW", "Feasibility input viewed by sub activity", "FeasibilityInput", "/market-study/feasibility-input/get-by-sub-activity/" + subActivityId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/feasibility-input/delete/{id}")
    public ResponseEntity<?> deleteFeasibilityInput(
            Principal principal,
            @PathVariable Long id) {

        try {
            WorkflowResponse response =
                    service.deleteFeasibilityInput(id);
            log.info("Delete successful for id={}", id);

            logService.logs(
                    principal.getName(),
                    "DELETE",
                    "Feasibility input deleted successfully",
                    "FeasibilityInput",
                    "/market-study/feasibility-input/delete/" + id
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
