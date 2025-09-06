package com.metaverse.workflow.nontrainingExpenditures.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.BenchmarkingStudyRequest;
import com.metaverse.workflow.nontrainingExpenditures.service.BenchmarkingStudyService;
import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingResourceExpenditureDTO;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/benchmarking-study")
@RequiredArgsConstructor
public class BenchmarkingStudyController {

    private final BenchmarkingStudyService service;

    @PostMapping(path="/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart String benchmarkingStudy ,@RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BenchmarkingStudyRequest benchmarkingStudyRequest = objectMapper.readValue(benchmarkingStudy, BenchmarkingStudyRequest.class);
            return ResponseEntity.ok(service.createBenchmarkingStudy(benchmarkingStudyRequest,file));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{benchmarkingStudyId}")
    public ResponseEntity<?> update(@PathVariable Long benchmarkingStudyId,
                                    @RequestBody BenchmarkingStudyRequest request) {
        try {
            WorkflowResponse response = service.updateBenchmarkingStudy(benchmarkingStudyId, request);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/by-subactivity")
    public ResponseEntity<?> getBySubActivityId(@PathParam("subActivityId") Long subActivityId) {
        try {
            WorkflowResponse response = service.getBenchmarkingStudyBySubActivityId(subActivityId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/{benchmarkingStudyId}")
    public ResponseEntity<?> getById(@PathVariable Long benchmarkingStudyId) {
        try {
            WorkflowResponse response = service.getBenchmarkingStudyById(benchmarkingStudyId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/{benchmarkingStudyId}")
    public ResponseEntity<?> delete(@PathVariable Long benchmarkingStudyId) {
        try {
            WorkflowResponse response = service.deleteBenchmarkingStudy(benchmarkingStudyId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
