package com.metaverse.workflow.tgtpc_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.tgtpc_handholding.request_dto.RawMaterialSupportRequest;
import com.metaverse.workflow.tgtpc_handholding.service.RawMaterialSupportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/tgtpc/raw-material-support")
@Tag(name = "Handholding Support", description = "Raw Material Support Management")
@RequiredArgsConstructor
public class RawMaterialSupportController {

    private final RawMaterialSupportService service;
    private final ActivityLogService logService;

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(Principal principal, @RequestBody RawMaterialSupportRequest request) {
        try {
            WorkflowResponse response = service.save(request);

            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Raw Material Support created successfully",
                    "RawMaterialSupport",
                    "/raw-material-support/save"
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(Principal principal, @PathVariable Long id, @RequestBody RawMaterialSupportRequest request) {
        try {
            WorkflowResponse response = service.update(id, request);

            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "Raw Material Support updated successfully",
                    "RawMaterialSupport",
                    "/raw-material-support/update/" + id
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(Principal principal, @PathVariable Long id) {
        try {
            WorkflowResponse response = service.getById(id);

            logService.logs(
                    principal.getName(),
                    "VIEW",
                    "Raw Material Support viewed",
                    "RawMaterialSupport",
                    "/raw-material-support/get/" + id
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/get-by-sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivity(Principal principal, @PathVariable Long subActivityId) {
        WorkflowResponse response = service.getBySubActivityId(subActivityId);

        logService.logs(
                principal.getName(),
                "VIEW",
                "Raw Material Support viewed by sub activity",
                "RawMaterialSupport",
                "/raw-material-support/get-by-sub-activity/" + subActivityId
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(Principal principal, @PathVariable Long id) {
        try {
            WorkflowResponse response = service.delete(id);

            logService.logs(
                    principal.getName(),
                    "DELETE",
                    "Raw Material Support deleted successfully",
                    "RawMaterialSupport",
                    "/raw-material-support/delete/" + id
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
