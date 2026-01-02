package com.metaverse.workflow.aleap_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.MachineryIdentificationRequest;
import com.metaverse.workflow.aleap_handholding.service.MachineryIdentificationService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/handholding-support/machinery-identification")
@Tag(name = "Handholding Support", description = "Machinery Identification APIs")
@RequiredArgsConstructor
public class MachineryIdentificationController {

    private final MachineryIdentificationService service;
    private final ActivityLogService logService;


    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestBody MachineryIdentificationRequest request) {

        try {
            WorkflowResponse response = service.save(request);

            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Machinery Identification saved successfully",
                    "MachineryIdentification",
                    "/machinery-identification/save"
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    /* ========================= UPDATE ========================= */

    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            Principal principal,
            @PathVariable Long id,
            @RequestBody MachineryIdentificationRequest request) {

        try {
            WorkflowResponse response = service.update(id, request);

            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "Machinery Identification updated successfully",
                    "MachineryIdentification",
                    "/machinery-identification/update/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
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

            logService.logs(
                    principal.getName(),
                    "VIEW",
                    "Machinery Identification viewed",
                    "MachineryIdentification",
                    "/machinery-identification/get/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
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

        logService.logs(
                principal.getName(),
                "VIEW",
                "Machinery Identification viewed by sub activity",
                "MachineryIdentification",
                "/machinery-identification/get-by-sub-activity/" + subActivityId
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

            logService.logs(
                    principal.getName(),
                    "DELETE",
                    "Machinery Identification deleted successfully",
                    "MachineryIdentification",
                    "/machinery-identification/delete/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
