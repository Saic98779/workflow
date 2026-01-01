package com.metaverse.workflow.formalisationcompliance.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.formalisationcompliance.dto.FormalisationComplianceRequest;
import com.metaverse.workflow.formalisationcompliance.service.FormalisationComplianceService;
import com.metaverse.workflow.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/formalisation-compliance")
@RequiredArgsConstructor
public class FormalisationComplianceController {

    private final FormalisationComplianceService service;
    private final ActivityLogService logService;

    @PostMapping(path = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createFormalisationCompliance(Principal principal, @RequestPart("formalisationCompliance") String formalisationCompliance, @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            FormalisationComplianceRequest request =
                    objectMapper.readValue(formalisationCompliance, FormalisationComplianceRequest.class);

            WorkflowResponse response = service.create(request, file);

            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Formalisation Compliance created successfully",
                    "FormalisationCompliance",
                    "/formalisation-compliance/save"
            );

            return ResponseEntity.ok(response);

        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WorkflowResponse.error("Invalid JSON format"));
        }
    }


    @PutMapping(path = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateFormalisationCompliance(Principal principal, @PathVariable Long id, @RequestPart("formalisationCompliance") String formalisationCompliance, @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            FormalisationComplianceRequest request = objectMapper.readValue(formalisationCompliance, FormalisationComplianceRequest.class);

            WorkflowResponse response = service.update(id, request, file);

            logService.logs(principal.getName(), "UPDATE", "Formalisation Compliance updated successfully", "FormalisationCompliance", "/formalisation-compliance/update/" + id);

            return ResponseEntity.ok(response);

        } catch (JsonProcessingException e) {
            return ResponseEntity
                    .badRequest()
                    .body(WorkflowResponse.error("Invalid JSON format"));
        }
    }


    // GET BY ID
    @GetMapping("/get/{formalisationComplianceId}")
    public WorkflowResponse getById(@PathVariable Long formalisationComplianceId) {
        return service.getById(formalisationComplianceId);
    }

    // DELETE
    @DeleteMapping("/delete/{formalisationComplianceId}")
    public WorkflowResponse delete(@PathVariable Long formalisationComplianceId) {
        return service.delete(formalisationComplianceId);
    }

    @GetMapping("/sub-activity/{subActivityId}")
    public WorkflowResponse getBySubActivityId(
            @PathVariable Long subActivityId) {

        return service.getByNonTrainingSubActivityId(subActivityId);
    }

}

