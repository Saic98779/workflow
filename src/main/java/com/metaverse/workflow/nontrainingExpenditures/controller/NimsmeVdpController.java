package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NimsmeVdpRequest;
import com.metaverse.workflow.nontrainingExpenditures.service.NimsmeVdpService;
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
@RequestMapping("nimsme-vdp")
@RequiredArgsConstructor
public class NimsmeVdpController {

    private final NimsmeVdpService service;
    private final ActivityLogService logService;
    private static final Logger log = LogManager.getLogger(NimsmeVdpController.class);

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(Principal principal,
            @RequestPart("nimsmeVdpRequest") String request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest servletRequest) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NimsmeVdpRequest nimsmeVdpRequest = objectMapper.readValue(request, NimsmeVdpRequest.class);
            WorkflowResponse response = service.save(nimsmeVdpRequest, file);
            log.info("Save successful for NIMSME VDP");
            logService.logs(
                    principal.getName(),
                    "SAVE",
                    "NIMSME VDP saved successfully",
                    "NimsmeVDP",
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
            @RequestPart("nimsmeVdpRequest") String request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest servletRequest) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NimsmeVdpRequest nimsmeVdpRequest =
                    objectMapper.readValue(request, NimsmeVdpRequest.class);

            WorkflowResponse response = service.update(id, nimsmeVdpRequest, file);

            log.info("Update successful for NIMSME VDP id={}", id);
            logService.logs(
                    principal.getName(),
                    "UPDATE",
                    "NIMSME VDP updated successfully",
                    "NimsmeVDP",
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
            log.info("getById successful for NIMSME VDP id={}", id);
            return ResponseEntity.ok(response);

        } catch (DataException e) {
            log.error("DataException in getById(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(Principal principal, @PathVariable Long id,
            HttpServletRequest servletRequest) {
        try {
            WorkflowResponse response = service.delete(id);

            log.info("Delete successful for NIMSME VDP id={}", id);
            logService.logs(
                    principal.getName(),
                    "DELETE",
                    "NIMSME VDP deleted successfully",
                    "NimsmeVDP",
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
        WorkflowResponse response = service.getBySubActivityId(subActivityId);
        log.info("getBySubActivityId successful for subActivityId={}", subActivityId);
        return ResponseEntity.ok(response);
    }
}
