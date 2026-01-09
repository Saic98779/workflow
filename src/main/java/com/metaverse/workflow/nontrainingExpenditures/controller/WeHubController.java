package com.metaverse.workflow.nontrainingExpenditures.controller;


import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.login.service.AuthRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.CorpusDebitFinancing;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubHandholdingRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubSDGRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubSelectedCompaniesRequest;
import com.metaverse.workflow.nontrainingExpenditures.service.WeHubService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.Header;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.event.spi.PreInsertEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Non Training", description = "WeHub APIs")
public class WeHubController {
    private final WeHubService service;
    private  final ActivityLogService logService;
    private final static Logger log =  LogManager.getLogger(WeHubController.class);

    @PostMapping("/save")
    public ResponseEntity<?> create(@RequestBody WeHubSelectedCompaniesRequest request, Principal principal) {
        try {
            WorkflowResponse response = service.create(request);
            logService.logs(principal.getName(), "SAVE",
                    "WeHub Selected Companies created successfully",
                    "WeHubSelectedCompanies",
                    "/save");
            log.info("WeHub Selected Companies created successfully");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping("/{candidateId}")
    public ResponseEntity<?> update(@PathVariable Long candidateId,
                                    @RequestBody WeHubSelectedCompaniesRequest request,Principal principal) {
        try {
            WorkflowResponse response = service.update(candidateId, request);
            logService.logs(principal.getName(), "UPDATE",
                    "WeHub Selected Companies updated successfully | ID: " + candidateId,
                    "WeHubSelectedCompanies",
                    "" + candidateId);
            log.info("WeHub Selected Companies updated successfully");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }


    @GetMapping("/by-subactivity")
    public ResponseEntity<?> getBySubActivityId(@PathParam("subActivityId") Long subActivityId) {
        try {
            WorkflowResponse response = service.getBySubActivityId(subActivityId);
            log.info("WeHub Selected Companies retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/{candidateId}")
    public ResponseEntity<?> delete(@PathVariable Long candidateId,Principal principal) {
        try {
            WorkflowResponse response = service.delete(candidateId);
            logService.logs(principal.getName(), "DELETE",
                    "WeHub Selected Companies deleted successfully | ID: " + candidateId,
                    "WeHubSelectedCompanies",
                    "" + candidateId);
            log.info("WeHub Selected Companies deleted successfully");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/{candidateId}")
    public ResponseEntity<?> GetById(@PathVariable Long candidateId) {
        try {
            WorkflowResponse response = service.getById(candidateId);
            log.info("WeHub Selected Companies retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }


    @PostMapping("/handholding/save")
    public ResponseEntity<?> create(@RequestBody WeHubHandholdingRequest request,Principal principal) {
        try {
            logService.logs(principal.getName(), "SAVE",
                    "WeHub Handholding created successfully",
                    "WeHubHandholding",
                    "/handholding/save");
            log.info("WeHub Handholding created successfully");
            return ResponseEntity.ok(service.createHandholding(request));

        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping("/handholding/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long handholdingId,
                                    @RequestBody WeHubHandholdingRequest request,Principal principal) {
        try {
            logService.logs(principal.getName(), "UPDATE",
                    "WeHub Handholding updated successfully | ID: " + handholdingId,
                    "WeHubHandholding",
                    "/handholding/update/" + handholdingId);
            log.info("WeHub Handholding updated successfully");
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
    public ResponseEntity<?> deleteHandholding(@PathVariable("id") Long handholdingId,Principal principal) {
        try {
            logService.logs(principal.getName(), "DELETE",
                    "WeHub Handholding deleted successfully | ID: " + handholdingId,
                    "WeHubHandholding",
                    "/handholding/" + handholdingId);
            log.info("WeHub Handholding deleted successfully");
            return ResponseEntity.ok(service.deleteHandholding(handholdingId));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PostMapping("/wehub_sdg/save")
    public ResponseEntity<?> create(@RequestBody WeHubSDGRequest request,Principal principal) {
        try {
            WorkflowResponse response = service.createWeHubSDG(request);
            logService.logs(principal.getName(), "SAVE",
                    "WeHub SDG created successfully",
                    "WeHubSDG",
                    "/wehub_sdg/save");
            log.info("WeHub SDG created successfully");
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
    public ResponseEntity<?> update(@PathVariable Long weHubSDGId, @RequestBody WeHubSDGRequest request,Principal principal) {
        try {
            WorkflowResponse response = service.updateWeHubSDG(weHubSDGId, request);
            logService.logs(principal.getName(), "UPDATE",
                    "WeHub SDG updated successfully | ID: " + weHubSDGId,
                    "WeHubSDG",
                    "/wehub_sdg/update/" + weHubSDGId);
            log.info("WeHub SDG updated successfully");
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
    public ResponseEntity<?> getByActivityId(@PathVariable Long nonTrainingSubActivityId,Principal principal) {
        try {
            WorkflowResponse response = service.getWeHubSDGByActivityId(nonTrainingSubActivityId);
            log.info("WeHub SDG retrieved successfully"+principal.getName());
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
    public ResponseEntity<?> getWeHubSDGById(@PathVariable Long weHubSDGId,Principal principal) {
        try {
            WorkflowResponse response = service.getWeHubSDGById(weHubSDGId);
            log.info("getWeHubSDGById WeHub SDG retrieved successfully"+principal.getName());
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
    public ResponseEntity<?> deleteWeHubSDG(@PathVariable Long weHubSDGId,Principal principal) {
        try {
            WorkflowResponse response = service.deleteWeHubSDG(weHubSDGId);
            logService.logs(principal.getName(), "DELETE",
                    "WeHub SDG deleted successfully | ID: " + weHubSDGId,
                    "WeHubSDG",
                    "/wehub_sdg/" + weHubSDGId);
            log.info("WeHub SDG deleted successfully");
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

    @GetMapping(value = "/corpusDebitFinancing", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> corpusDebitFinancing() {
        try {
            log.info("Corpus Debit Financing successfully");
            return ResponseEntity.ok(
                    WorkflowResponse.builder()
                            .status(200)
                            .message("fetched")
                            .data(service.corpusDebitFinancing())
                            .build()
            );
        } catch (RestClientException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}
