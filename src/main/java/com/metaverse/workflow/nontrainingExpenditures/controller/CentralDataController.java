package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.CentralDataRequest;
import com.metaverse.workflow.nontrainingExpenditures.service.NIMSMECentralDataService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/central-data")
@RequiredArgsConstructor
public class CentralDataController {

    private final NIMSMECentralDataService service;
    private final ActivityLogService logService;

    @PostMapping(path = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart String centralData,
                                    @RequestPart(value = "file", required = false) MultipartFile file, Principal principal) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CentralDataRequest centralDataRequest = objectMapper.readValue(centralData, CentralDataRequest.class);
            WorkflowResponse response = service.createCentralData(centralDataRequest, file);
            logService.logs(principal.getName(), "SAVE", "Central data created successfully", "CentralData", "/central-data/save");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/update/{centralDataId}")
    public ResponseEntity<?> update(@PathVariable Long centralDataId,
                                    @RequestBody CentralDataRequest request,Principal principal) {
        try {
            WorkflowResponse response = service.updateCentralData(centralDataId, request);
            logService.logs(principal.getName(), "UPDATE", "Central data updated successfully | ID: " + centralDataId, "CentralData", "/central-data/update/"+centralDataId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/by-subactivity")
    public ResponseEntity<?> getBySubActivityId(@PathParam("subActivityId") Long subActivityId) {
        try {
            WorkflowResponse response = service.getCentralDataBySubActivityId(subActivityId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/{centralDataId}")
    public ResponseEntity<?> getById(@PathVariable Long centralDataId) {
        try {
            WorkflowResponse response = service.getCentralDataById(centralDataId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/{centralDataId}")
    public ResponseEntity<?> delete(@PathVariable Long centralDataId,Principal principal) {
        try {
            WorkflowResponse response = service.deleteCentralData(centralDataId);
            logService.logs(principal.getName(), "DELETE", "Central data deleted successfully | ID: " + centralDataId, "CentralData", "/central-data/"+centralDataId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
