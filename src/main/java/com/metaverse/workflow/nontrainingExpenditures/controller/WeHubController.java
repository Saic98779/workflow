package com.metaverse.workflow.nontrainingExpenditures.controller;


import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.CorpusDebitFinancing;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubHandholdingRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubSDGRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubSelectedCompaniesRequest;
import com.metaverse.workflow.nontrainingExpenditures.service.WeHubService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WeHubController {
    private final WeHubService service;

    @Value("${tihcl.corpus.key}")
    private String API_KEY;

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


    @PostMapping("/handholding/save")
    public ResponseEntity<?> create(@RequestBody WeHubHandholdingRequest request) {
        try {
            return ResponseEntity.ok(service.createHandholding(request));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping("/handholding/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long handholdingId,
                                    @RequestBody WeHubHandholdingRequest request) {
        try {
            return ResponseEntity.ok(service.updateHandholding(handholdingId, request));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/handholding/sub-activity/{subActivityId}")
    public ResponseEntity<?> getByHandholdingSubActivityId(@PathVariable Long subActivityId) {
        try {
            return ResponseEntity.ok(service.getHandholdingBySubActivityId(subActivityId));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/handholding/{id}")
    public ResponseEntity<?> getHandHoldingById(@PathVariable("id") Long handholdingId) {
        try {
            return ResponseEntity.ok(service.getHandholdingById(handholdingId));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/handholding/{id}")
    public ResponseEntity<?> deleteHandholding(@PathVariable("id") Long handholdingId) {
        try {
            return ResponseEntity.ok(service.deleteHandholding(handholdingId));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PostMapping("/wehub_sdg/save")
    public ResponseEntity<?> create(@RequestBody WeHubSDGRequest request) {
        try {
            WorkflowResponse response = service.createWeHubSDG(request);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return ResponseEntity.status(400).body(
                    WorkflowResponse.builder()
                            .status(400)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("/wehub_sdg/update{weHubSDGId}")
    public ResponseEntity<?> update(@PathVariable Long eeHubSDGId, @RequestBody WeHubSDGRequest request) {
        try {
            WorkflowResponse response = service.updateWeHubSDG(eeHubSDGId, request);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return ResponseEntity.status(400).body(
                    WorkflowResponse.builder()
                            .status(400)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/wehub_sdg/sub-activity/{nonTrainingSubActivityId}")
    public ResponseEntity<?> getByActivityId(@PathVariable Long nonTrainingSubActivityId) {
        try {
            WorkflowResponse response = service.getWeHubSDGByActivityId(nonTrainingSubActivityId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return ResponseEntity.status(400).body(
                    WorkflowResponse.builder()
                            .status(400)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/wehub_sdg/{weHubSDGId}")
    public ResponseEntity<?> getWeHubSDGById(@PathVariable Long weHubSDGId) {
        try {
            WorkflowResponse response = service.getWeHubSDGById(weHubSDGId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return ResponseEntity.status(400).body(
                    WorkflowResponse.builder()
                            .status(400)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/wehub_sdg/{weHubSDGId}")
    public ResponseEntity<?> deleteWeHubSDG(@PathVariable Long eeHubSDGId) {
        try {
            WorkflowResponse response = service.deleteWeHubSDG(eeHubSDGId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return ResponseEntity.status(400).body(
                    WorkflowResponse.builder()
                            .status(400)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/selected-organizations")
    public WorkflowResponse getSelectedOrganization()
    {
        return service.getSelectedOrganization();
    }

    @GetMapping("/corpusDebitFinancing")
    public ResponseEntity corpusDebitFinancing() {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<List> response = restTemplate.exchange(
                    "https://tihcl.com/tihcl/api/tihcl/corpusDebitFinancing",
                    HttpMethod.GET,
                    entity,
                    List.class
            );
            return response;
        } catch (RestClientException e) {
         return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
