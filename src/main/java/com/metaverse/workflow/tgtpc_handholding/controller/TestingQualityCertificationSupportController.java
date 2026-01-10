package com.metaverse.workflow.tgtpc_handholding.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.tgtpc_handholding.request_dto.TestingQualityCertificationSupportRequest;
import com.metaverse.workflow.tgtpc_handholding.service.TestingQualityCertificationSupportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/tgtpc/testing-quality-certification")
@Tag(name = "Handholding Support", description = "Testing Quality Certification Support APIs")
@RequiredArgsConstructor
public class TestingQualityCertificationSupportController {

    private final TestingQualityCertificationSupportService service;
    private final ActivityLogService logService;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestPart("testingRequest") String request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest servletRequest) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TestingQualityCertificationSupportRequest testingRequest =
                    objectMapper.readValue(request, TestingQualityCertificationSupportRequest.class);

            WorkflowResponse response = service.save(testingRequest, file);

            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Testing Quality Certification Support saved successfully",
                    "TestingQualityCertificationSupport",
                    servletRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            Principal principal,
            @PathVariable Long id,
            @RequestPart("testingRequest") String request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest servletRequest) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TestingQualityCertificationSupportRequest testingRequest =
                    objectMapper.readValue(request, TestingQualityCertificationSupportRequest.class);

            WorkflowResponse response = service.update(id, testingRequest, file);

            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "Testing Quality Certification Support updated successfully",
                    "TestingQualityCertificationSupport",
                    servletRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return RestControllerBase.error(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            WorkflowResponse response = service.getById(id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
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

            logService.logs(
                    principal.getName(),
                    "DELETE",
                    "Testing Quality Certification Support deleted successfully",
                    "TestingQualityCertificationSupport",
                    servletRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivityId(@PathVariable Long subActivityId) {
        WorkflowResponse response = service.getBySubActivityId(subActivityId);
        return ResponseEntity.ok(response);
    }
}
