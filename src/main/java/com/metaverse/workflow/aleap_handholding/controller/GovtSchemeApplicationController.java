package com.metaverse.workflow.aleap_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.GovtSchemeApplicationRequest;
import com.metaverse.workflow.aleap_handholding.service.GovtSchemeApplicationService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/govt-scheme-application")
@RequiredArgsConstructor
public class GovtSchemeApplicationController {

    private final GovtSchemeApplicationService service;
    private final ActivityLogService logService;

    @PostMapping("/save")
    public ResponseEntity<?> save(Principal principal, @RequestBody GovtSchemeApplicationRequest request) {
        try {
            WorkflowResponse response = service.save(request);
            logService.logs(principal.getName(), "SAVE", "Govt Scheme Application saved", "GovtSchemeApplication", "/govt-scheme-application/save");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(Principal principal, @PathVariable Long id, @RequestBody GovtSchemeApplicationRequest request) {
        try {
            WorkflowResponse response = service.update(id, request);
            logService.logs(principal.getName(), "UPDATE", "Govt Scheme Application updated", "GovtSchemeApplication", "/govt-scheme-application/update/" + id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(Principal principal, @PathVariable Long id) {
        try {
            WorkflowResponse response = service.delete(id);
            logService.logs(principal.getName(), "DELETE", "Govt Scheme Application deleted", "GovtSchemeApplication", "/govt-scheme-application/delete/" + id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivityId(@PathVariable Long subActivityId) {
        return ResponseEntity.ok(service.getBySubActivityId(subActivityId));
    }
}
