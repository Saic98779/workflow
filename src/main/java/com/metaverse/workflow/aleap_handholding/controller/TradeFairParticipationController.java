package com.metaverse.workflow.aleap_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.TradeFairParticipationRequest;
import com.metaverse.workflow.aleap_handholding.service.TradeFairParticipationService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/trade-fair-participation")
@RequiredArgsConstructor
public class TradeFairParticipationController {

    private final TradeFairParticipationService service;
    private final ActivityLogService logService;


    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestBody TradeFairParticipationRequest request) {

        try {
            WorkflowResponse response = service.save(request);

            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Trade fair participation saved successfully",
                    "TradeFairParticipation",
                    "/trade-fair-participation/save"
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
            @RequestBody TradeFairParticipationRequest request) {

        try {
            WorkflowResponse response = service.update(id, request);

            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "Trade fair participation updated successfully",
                    "TradeFairParticipation",
                    "/trade-fair-participation/update/" + id
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
                    "Trade fair participation viewed",
                    "TradeFairParticipation",
                    "/trade-fair-participation/get/" + id
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

        WorkflowResponse response =
                service.getByNonTrainingSubActivityId(subActivityId);

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

            logService.logs(
                    principal.getName(),
                    "DELETE",
                    "Trade fair participation deleted successfully",
                    "TradeFairParticipation",
                    "/trade-fair-participation/delete/" + id
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
