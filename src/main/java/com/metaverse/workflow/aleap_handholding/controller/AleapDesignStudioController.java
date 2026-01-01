package com.metaverse.workflow.aleap_handholding.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.AleapDesignStudioRequest;
import com.metaverse.workflow.aleap_handholding.request_dto.BusinessPlanRequest;
import com.metaverse.workflow.aleap_handholding.service.AleapDesignStudioService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/aleap-design-studio")
@RequiredArgsConstructor
public class AleapDesignStudioController {
    private final AleapDesignStudioService service;
    private final ActivityLogService logService; // Assuming you have a logging service

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestPart("aleapDesignStudioRequest") String request,
            @RequestPart(value = "image1", required = true) MultipartFile image1,
            @RequestPart(value = "image2", required = false) MultipartFile image2,
            @RequestPart(value = "image3", required = false) MultipartFile image3,
            HttpServletRequest servletRequest) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            AleapDesignStudioRequest aleapDesignStudioRequest =
                    objectMapper.readValue(request, AleapDesignStudioRequest.class);
            WorkflowResponse response = service.save(aleapDesignStudioRequest, image1, image2, image3);

            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Aleap Design Studio saved successfully",
                    "AleapDesignStudio",
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
            @RequestPart("aleapDesignStudioRequest") String request,
            @RequestPart(value = "image1", required = false) MultipartFile image1,
            @RequestPart(value = "image2", required = false) MultipartFile image2,
            @RequestPart(value = "image3", required = false) MultipartFile image3,
            HttpServletRequest servletRequest) {

        try {

            ObjectMapper objectMapper = new ObjectMapper();

            AleapDesignStudioRequest aleapDesignStudioRequest =
                    objectMapper.readValue(request, AleapDesignStudioRequest.class);

            WorkflowResponse response = service.update(id, aleapDesignStudioRequest, image1, image2, image3);

            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "Aleap Design Studio updated successfully",
                    "AleapDesignStudio",
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

    @GetMapping("/sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivityId(@PathVariable Long subActivityId) {
        WorkflowResponse response = service.getByNonTrainingSubActivityId(subActivityId);
        return ResponseEntity.ok(response);
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
                    "Aleap Design Studio deleted successfully",
                    "AleapDesignStudio",
                    servletRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

}
