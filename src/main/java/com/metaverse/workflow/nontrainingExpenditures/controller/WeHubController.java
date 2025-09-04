package com.metaverse.workflow.nontrainingExpenditures.controller;


import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubSelectedCompaniesRequest;
import com.metaverse.workflow.nontrainingExpenditures.service.WeHubService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
public class WeHubController {
    private final WeHubService service;
    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody WeHubSelectedCompaniesRequest request) {
        try {
            WorkflowResponse response = service.create(request);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping("/{candidateId}")
    public ResponseEntity<?> update(@PathVariable Long candidateId,
                                    @RequestBody WeHubSelectedCompaniesRequest request) {
        try {
            WorkflowResponse response = service.update(candidateId, request);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }


    @GetMapping("/by-subactivity")
    public ResponseEntity<?> getBySubActivityId(@PathParam("subActivityId") Long subActivityId) {
        try {
            WorkflowResponse response = service.getBySubActivityId(subActivityId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/{candidateId}")
    public ResponseEntity<?> delete(@PathVariable Long candidateId) {
        try {
            WorkflowResponse response = service.delete(candidateId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/{candidateId}")
    public ResponseEntity<?> GetById(@PathVariable Long candidateId) {
        try {
            WorkflowResponse response = service.getById(candidateId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
