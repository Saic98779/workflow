package com.metaverse.workflow.aleap_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.TradeFairParticipationRequest;
import com.metaverse.workflow.aleap_handholding.service.TradeFairParticipationService;
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
@RequestMapping("/handholding-support/trade-fair-participation")
@Tag(name = "Handholding Support", description = "Trade Fair Participation APIs")
@RequiredArgsConstructor
public class TradeFairParticipationController {

    private static final Logger log = LogManager.getLogger(TradeFairParticipationController.class);

    private final TradeFairParticipationService service;
    private final ActivityLogService logService;


    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestBody TradeFairParticipationRequest request) {
        try {
            WorkflowResponse response = service.save(request);
            log.info("Save successful for TradeFairParticipation");

            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Trade fair participation saved successfully",
                    "TradeFairParticipation",
                    "/trade-fair-participation/save"
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
            @RequestBody TradeFairParticipationRequest request) {

        try {
            WorkflowResponse response = service.update(id, request);
            log.info("Update successful for id={}", id);

            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "Trade fair participation updated successfully",
                    "TradeFairParticipation",
                    "/trade-fair-participation/update/" + id
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
                    "Trade fair participation viewed",
                    "TradeFairParticipation",
                    "/trade-fair-participation/get/" + id
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
                "Trade fair participation viewed by sub activity",
                "TradeFairParticipation",
                "/trade-fair-participation/get-by-sub-activity/" + subActivityId
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
                    "Trade fair participation deleted successfully",
                    "TradeFairParticipation",
                    "/trade-fair-participation/delete/" + id
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in delete(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }
}
