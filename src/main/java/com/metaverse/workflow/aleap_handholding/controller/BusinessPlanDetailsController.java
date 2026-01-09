package com.metaverse.workflow.aleap_handholding.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.BusinessPlanRequest;
import com.metaverse.workflow.aleap_handholding.service.BusinessPlanDetailsService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/handholding-support/business-plan")
@Tag(name = "Handholding Support", description = "Bank/NBFC & Design Studio APIs")
@RequiredArgsConstructor
public class BusinessPlanDetailsController {

 private final BusinessPlanDetailsService service;
    private final ActivityLogService logService;
    private static final Logger log = LogManager.getLogger(BusinessPlanDetailsController.class);

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestPart("businessPlanRequest") String request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest servletRequest) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BusinessPlanRequest businessPlanRequest =
                    objectMapper.readValue(request, BusinessPlanRequest.class);
            WorkflowResponse response = service.save(businessPlanRequest, file);

            log.info("Save successful for BusinessPlanDetails");
            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Business Plan saved successfully",
                    "BusinessPlanDetails",
                    servletRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in save(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        } catch (JsonMappingException e) {
            log.error("JsonMappingException in save(): {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException in save(): {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            Principal principal,
            @PathVariable Long id,
            @RequestPart("businessPlanRequest") String request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest servletRequest) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BusinessPlanRequest businessPlanRequest =
                    objectMapper.readValue(request, BusinessPlanRequest.class);
            WorkflowResponse response = service.update(id, businessPlanRequest, file);

            log.info("Update successful for id={}", id);
            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "Business Plan updated successfully",
                    "BusinessPlanDetails",
                    servletRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in update(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        } catch (JsonMappingException e) {
            log.error("JsonMappingException in update(): {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException in update(): {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            WorkflowResponse response = service.getById(id);
            log.info("getById successful for id={}", id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in getById(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            Principal principal,
            @PathVariable Long id,
            HttpServletRequest servletRequest) {

        try {
            WorkflowResponse response = service.delete(id);
            log.info("Delete successful for id={}", id);

            logService.logs(
                    principal.getName(),
                    "DELETE",
                    "Business Plan deleted successfully",
                    "BusinessPlanDetails",
                    servletRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in delete(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivityId(@PathVariable Long subActivityId) {
        WorkflowResponse response = service.getByNonTrainingSubActivityId(subActivityId);
        log.info("getBySubActivityId successful for subActivityId={}", subActivityId);
        return ResponseEntity.ok(response);
    }
}
