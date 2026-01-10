package com.metaverse.workflow.aleap_handholding.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.FormalisationComplianceRequest;
import com.metaverse.workflow.aleap_handholding.service.FormalisationComplianceService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/formalisation-compliance")
@Tag(name = "Handholding Support", description = "Formalisation Compliance APIs")
@RequiredArgsConstructor
public class FormalisationComplianceController {

    private final FormalisationComplianceService service;
    private final ActivityLogService logService;
    private static final Logger log = LogManager.getLogger(FormalisationComplianceController.class);

    @PostMapping(path = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createFormalisationCompliance(Principal principal, @RequestPart("formalisationCompliance") String formalisationCompliance, @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FormalisationComplianceRequest request =
                    objectMapper.readValue(formalisationCompliance, FormalisationComplianceRequest.class);

            WorkflowResponse response = service.create(request, file);

            log.info("Create successful for FormalisationCompliance");
            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Formalisation Compliance created successfully",
                    "FormalisationCompliance",
                    "/formalisation-compliance/save"
            );

            return ResponseEntity.ok(response);

        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException in createFormalisationCompliance(): {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WorkflowResponse.error("Invalid JSON format"));
        }
    }

    @PutMapping(path = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateFormalisationCompliance(Principal principal, @PathVariable Long id, @RequestPart("formalisationCompliance") String formalisationCompliance, @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FormalisationComplianceRequest request =
                    objectMapper.readValue(formalisationCompliance, FormalisationComplianceRequest.class);

            WorkflowResponse response = service.update(id, request, file);

            logService.logs(principal.getName(), "UPDATE", "Formalisation Compliance updated successfully", "FormalisationCompliance", "/formalisation-compliance/update/" + id);
            log.error("Formalisation Compliance updated successfully  ID : "+id);

            return ResponseEntity.ok(response);

        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException in updateFormalisationCompliance(): {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(WorkflowResponse.error("Invalid JSON format"));
        }
    }

    // GET BY ID
    @GetMapping("/get/{formalisationComplianceId}")
    public WorkflowResponse getById(@PathVariable Long formalisationComplianceId) {
        WorkflowResponse response = service.getById(formalisationComplianceId);
        log.info("getById successful for formalisationComplianceId={}", formalisationComplianceId);
        return response;
    }

    // DELETE
    @DeleteMapping("/delete/{formalisationComplianceId}")
    public WorkflowResponse delete(@PathVariable Long formalisationComplianceId) {
        WorkflowResponse response = service.delete(formalisationComplianceId);
        log.info("Delete successful for formalisationComplianceId={}", formalisationComplianceId);
        return response;
    }

    @GetMapping("/sub-activity/{subActivityId}")
    public WorkflowResponse getBySubActivityId(@PathVariable Long subActivityId) {
        WorkflowResponse response = service.getByNonTrainingSubActivityId(subActivityId);
        log.info("getBySubActivityId successful for subActivityId={}", subActivityId);
        return response;
    }
}
