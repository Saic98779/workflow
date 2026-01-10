package com.metaverse.workflow.tgtpc_handholding.controller;


import com.metaverse.workflow.tgtpc_handholding.request_dto.TGTPC4NTHandholdingRequest;
import com.metaverse.workflow.tgtpc_handholding.service.TGTPC4NTHandholdingService;
import com.metaverse.workflow.activitylog.ActivityLogService;
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
@RequestMapping("/tgtpc4-nt-handholding")
@Tag(name = "Handholding Support", description = "TGTPC4 NT Handholding")
@RequiredArgsConstructor
public class TGTPC4NTHandholdingController {

    private final TGTPC4NTHandholdingService service;
    private final ActivityLogService logService;

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(Principal principal, @RequestBody TGTPC4NTHandholdingRequest request) {

        try {
            WorkflowResponse response = service.save(request);

            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "TGTPC4 NT Handholding created successfully",
                    "TGTPC4NTHandholding",
                    "/tgtpc4-nt-handholding/save"
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(Principal principal, @PathVariable Long id, @RequestBody TGTPC4NTHandholdingRequest request) {

        try {
            WorkflowResponse response = service.update(id, request);

            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "TGTPC4 NT Handholding updated successfully",
                    "TGTPC4NTHandholding",
                    "/tgtpc4-nt-handholding/update/" + id
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
                    "TGTPC4 NT Handholding viewed",
                    "TGTPC4NTHandholding",
                    "/tgtpc4-nt-handholding/get/" + id
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
                "TGTPC4 NT Handholding viewed by sub activity",
                "TGTPC4NTHandholding",
                "/tgtpc4-nt-handholding/get-by-sub-activity/" + subActivityId
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
                    "TGTPC4 NT Handholding deleted successfully",
                    "TGTPC4NTHandholding",
                    "/tgtpc4-nt-handholding/delete/" + id
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}

