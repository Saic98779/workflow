package com.metaverse.workflow.aleap_handholding.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.aleap_handholding.request_dto.AleapDesignStudioRequest;
import com.metaverse.workflow.aleap_handholding.service.AleapDesignStudioService;
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
@RequestMapping("/handholding-support/aleap-design-studio")
@Tag(name = "Handholding Support", description = "Bank/NBFC & Design Studio APIs")
@RequiredArgsConstructor
public class AleapDesignStudioController {
    private final AleapDesignStudioService service;
    private final ActivityLogService logService;
    private static final Logger log = LogManager.getLogger(AleapDesignStudioController.class);

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            Principal principal,
            @RequestPart("aleapDesignStudioRequest") String request,
            @RequestPart(value = "image1", required = true) MultipartFile image1,
            @RequestPart(value = "image2", required = false) MultipartFile image2,
            @RequestPart(value = "image3", required = false) MultipartFile image3,
            HttpServletRequest servletRequest) {

        log.info("Entering save() with principal={}, URI={}", principal.getName(), servletRequest.getRequestURI());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AleapDesignStudioRequest aleapDesignStudioRequest =
                    objectMapper.readValue(request, AleapDesignStudioRequest.class);

            log.debug("Parsed AleapDesignStudioRequest: {}", aleapDesignStudioRequest);

            WorkflowResponse response = service.save(aleapDesignStudioRequest, image1, image2, image3);

            log.info("Save successful for AleapDesignStudio");
            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "Aleap Design Studio saved successfully",
                    "AleapDesignStudio",
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
        } finally {
            log.info("Exiting save()");
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

        log.info("Entering update() with id={}, principal={}, URI={}", id, principal.getName(), servletRequest.getRequestURI());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AleapDesignStudioRequest aleapDesignStudioRequest =
                    objectMapper.readValue(request, AleapDesignStudioRequest.class);

            log.debug("Parsed AleapDesignStudioRequest for update: {}", aleapDesignStudioRequest);

            WorkflowResponse response = service.update(id, aleapDesignStudioRequest, image1, image2, image3);

            log.info("Update successful for id={}", id);
            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "Aleap Design Studio updated successfully",
                    "AleapDesignStudio",
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
        } finally {
            log.info("Exiting update()");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        log.info("Entering getById() with id={}", id);
        try {
            WorkflowResponse response = service.getById(id);
            log.info("getById successful for id={}", id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in getById(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        } finally {
            log.info("Exiting getById()");
        }
    }

    @GetMapping("/sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivityId(@PathVariable Long subActivityId) {
        log.info("Entering getBySubActivityId() with subActivityId={}", subActivityId);
        WorkflowResponse response = service.getByNonTrainingSubActivityId(subActivityId);
        log.info("getBySubActivityId successful for subActivityId={}", subActivityId);
        log.info("Exiting getBySubActivityId()");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            Principal principal,
            @PathVariable Long id,
            HttpServletRequest servletRequest) {

        log.info("Entering delete() with id={}, principal={}, URI={}", id, principal.getName(), servletRequest.getRequestURI());
        try {
            WorkflowResponse response = service.delete(id);
            log.info("Delete successful for id={}", id);

            logService.logs(
                    principal.getName(),
                    "DELETE",
                    "Aleap Design Studio deleted successfully",
                    "AleapDesignStudio",
                    servletRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("DataException in delete(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

}
