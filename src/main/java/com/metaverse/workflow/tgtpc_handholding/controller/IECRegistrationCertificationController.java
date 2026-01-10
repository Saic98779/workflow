package com.metaverse.workflow.tgtpc_handholding.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.tgtpc_handholding.request_dto.IECRegistrationCertificationRequest;
import com.metaverse.workflow.tgtpc_handholding.service.IECRegistrationCertificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/tgtpc/iec-registration-certification")
@Tag(name = "Handholding Support", description = "IEC Registration and Certification Management")
@RequiredArgsConstructor
public class IECRegistrationCertificationController {

    private final IECRegistrationCertificationService service;
    private final ActivityLogService logService;

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(Principal principal, @RequestBody IECRegistrationCertificationRequest request) {
        try {
            WorkflowResponse response = service.save(request);

            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "IEC Registration/Certification created successfully",
                    "IECRegistrationCertification",
                    "/iec-registration-certification/save"
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping(path = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(Principal principal, @PathVariable Long id, @RequestBody IECRegistrationCertificationRequest request) {
        try {
            WorkflowResponse response = service.update(id, request);

            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "IEC Registration/Certification updated successfully",
                    "IECRegistrationCertification",
                    "/iec-registration-certification/update/" + id
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
                    "IEC Registration/Certification viewed",
                    "IECRegistrationCertification",
                    "/iec-registration-certification/get/" + id
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
                "IEC Registration/Certification viewed by sub activity",
                "IECRegistrationCertification",
                "/iec-registration-certification/get-by-sub-activity/" + subActivityId
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
                    "IEC Registration/Certification deleted successfully",
                    "IECRegistrationCertification",
                    "/iec-registration-certification/delete/" + id
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
