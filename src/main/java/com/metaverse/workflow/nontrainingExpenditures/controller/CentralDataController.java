package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.CentralDataRequest;
import com.metaverse.workflow.nontrainingExpenditures.service.NIMSMECentralDataService;
import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingExpenditureRemarksDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/central-data")
@RequiredArgsConstructor
@Tag(name = "Non Training", description = "Central Data APIs")
public class CentralDataController {

    private final NIMSMECentralDataService service;
    private final ActivityLogService logService;
    private static final Logger log = LogManager.getLogger(CentralDataController.class);


    @PostMapping(path = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart String centralData,
                                    @RequestPart(value = "file", required = false) MultipartFile file, Principal principal) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CentralDataRequest centralDataRequest = objectMapper.readValue(centralData, CentralDataRequest.class);
            WorkflowResponse response = service.createCentralData(centralDataRequest, file);
            logService.logs(principal.getName(), "SAVE", "Central data created successfully", "CentralData", "/central-data/save");
            log.info("SAVE: central-data created successfully");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("SAVE: central-data created successfully failed", e);
            return RestControllerBase.error(e);
        } catch (JsonMappingException e) {
            log.error("SAVE: central-data created successfully failed", e);
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            log.error("SAVE: central-data created successfully failed", e);
            throw new RuntimeException(e);
        }
    }

    @PutMapping(path = "/update/{centralDataId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@PathVariable Long centralDataId,
                                    @RequestPart String request,Principal principal,MultipartFile file) {
        try { //CentralDataRequest
            ObjectMapper objectMapper = new ObjectMapper();
            CentralDataRequest dto = objectMapper.readValue(request,CentralDataRequest.class);
            WorkflowResponse response = service.updateCentralData(centralDataId, dto,file);
            logService.logs(principal.getName(), "UPDATE", "Central data updated successfully | ID: " + centralDataId, "CentralData", "/central-data/update/"+centralDataId);
            log.info("UPDATE: central-data updated successfully");
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            log.error("UPDATE: central-data updated successfully failed", e);
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder()
                            .message("FAILURE: Invalid JSON format — " + e.getOriginalMessage()).status(400).build());

        } catch (IOException e) {
            log.error("UPDATE: central-data updated successfully failed", e);
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder()
                            .message("FAILURE: File operation failed — " + e.getMessage()).status(500).build());

        } catch (RuntimeException e) {
            log.error("UPDATE: central-data updated successfully failed", e);
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder()
                            .message("FAILURE: " + e.getMessage()).status(400).build());

        } catch (Exception e) {
            log.error("UPDATE: central-data updated successfully failed", e);
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder()
                            .message("FAILURE: Unexpected server error — " + e.getMessage()).status(500).build());
        }
    }

    @GetMapping("/by-subactivity")
    public ResponseEntity<?> getBySubActivityId(@PathParam("subActivityId") Long subActivityId) {
        try {
            WorkflowResponse response = service.getCentralDataBySubActivityId(subActivityId);
            log.info("getBySubActivityId: central-data response: " + response);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("getBySubActivityId: central-data response failed", e);
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/{centralDataId}")
    public ResponseEntity<?> getById(@PathVariable Long centralDataId) {
        try {
            WorkflowResponse response = service.getCentralDataById(centralDataId);
            log.info("getById: central-data response: " + response);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            log.error("getById: central-data response failed", e);
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/{centralDataId}")
    public ResponseEntity<?> delete(@PathVariable Long centralDataId,Principal principal) {
        try {
            WorkflowResponse response = service.deleteCentralData(centralDataId);
            logService.logs(principal.getName(), "DELETE", "Central data deleted successfully | ID: " + centralDataId, "CentralData", "/central-data/"+centralDataId);
            log.info("DELETE: central-data deleted successfully");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping("/save/remarks/")
    public ResponseEntity<?> addingRemarksToResourceExpenditure(Principal principal, @RequestBody NonTrainingExpenditureRemarksDTO remarksDTO,
                                                                @RequestParam(value = "status", required = false) BillRemarksStatus status,
                                                                HttpServletRequest servletRequest) {
        try {
            log.info("Updated Remarks successfully");
            return  ResponseEntity.ok(service.addRemarkOrResponse(remarksDTO, status));
        } catch (DataException e) {
            log.error("Updated Remarks failed", e);
            return RestControllerBase.error(e);
        }
    }
}
