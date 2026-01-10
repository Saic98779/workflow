package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NIMSMEContentDetailsDto;
import com.metaverse.workflow.nontrainingExpenditures.service.NIMSMEContentDetailsService;
import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingExpenditureRemarksDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/central-data")
@Tag(name = "Non Training", description = "NIMSME Content Details APIs")
public class NIMSMEContentDetailsController {

    private static final Logger log = LogManager.getLogger(NIMSMEContentDetailsController.class);
    @Autowired
    private NIMSMEContentDetailsService service;

    @PostMapping(path = "/content/save")
    public ResponseEntity<?> createContent(@RequestBody NIMSMEContentDetailsDto nimsmeContentDetailsDto) {
        ResponseEntity<?> response =
                ResponseEntity.ok(WorkflowResponse.builder().data(service.saveContent(nimsmeContentDetailsDto)).status(201).message("Saved successfully").build());
        log.info("Content saved successfully");
        return response;
    }

    @GetMapping(path = "/content/get-all")
    public ResponseEntity<?> getAllContent() {
        ResponseEntity<?> response = ResponseEntity.ok(WorkflowResponse.builder().data(service.getAllContent()).status(200).message("fetched successfully").build());
        log.info("Fetched all content successfully");
        return response;
    }

    @GetMapping("/content/{contentId}")
    public ResponseEntity<?> getContentById(@PathVariable Long contentId) {
        try {
            NIMSMEContentDetailsDto data = service.getContentById(contentId);
            log.info("Fetched content successfully for contentId={}", contentId);
            return ResponseEntity.ok(
                    WorkflowResponse.builder().data(data).status(200).message("Fetched successfully").build());
        } catch (RuntimeException e) {
            log.error("Error fetching content for contentId={}: {}", contentId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(WorkflowResponse.builder().status(404).message(e.getMessage()).build());
        }
    }

    @GetMapping("/content/subActivity/{nonTrainingSubActivityId}")
    public ResponseEntity<?> getContentByNonTrainingSubActivityId(@PathVariable Long nonTrainingSubActivityId) {
        try {
            List<NIMSMEContentDetailsDto> data = service.getContentByNonTrainingSubActivityId(nonTrainingSubActivityId);
            log.info("Fetched content successfully for subActivityId={}", nonTrainingSubActivityId);
            return ResponseEntity.ok(
                    WorkflowResponse.builder().data(data).status(200).message("Fetched successfully").build());
        } catch (RuntimeException e) {
            log.error("Error fetching content for subActivityId={}: {}", nonTrainingSubActivityId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(WorkflowResponse.builder().status(404).message(e.getMessage()).build());
        }
    }

    @PutMapping("/content/{contentId}")
    public ResponseEntity<?> updateContent(@PathVariable Long contentId, @RequestBody NIMSMEContentDetailsDto nimsmeContentDetailsDto) {

        try {
            NIMSMEContentDetailsDto updated = service.updateContent(contentId, nimsmeContentDetailsDto);
            log.info("Content updated successfully for contentId={}", contentId);
            return ResponseEntity.ok(
                    WorkflowResponse.builder().data(updated).status(200).message("Content updated successfully").build()
            );
        } catch (IllegalArgumentException e) {
            log.error("Invalid request in updateContent(): {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(WorkflowResponse.builder().status(400).message("Invalid request: " + e.getMessage()).build());
        } catch (Exception e) {
            log.error("Unexpected error in updateContent(): {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(WorkflowResponse.builder().status(500).message("An unexpected error occurred: " + e.getMessage()).build());
        }
    }

    @DeleteMapping("/content/{contentId}")
    public ResponseEntity<?> deleteContent(@PathVariable Long contentId) {
        try {
            service.deleteContent(contentId);
            log.info("Content deleted successfully for contentId={}", contentId);
            return ResponseEntity.ok(
                    WorkflowResponse.builder().status(200).message("Deleted successfully").build());
        } catch (IllegalArgumentException e) {
            log.error("Invalid request in deleteContent(): {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder().status(400).message("Invalid request: " + e.getMessage()).build());
        } catch (Exception e) {
            log.error("Unexpected error in deleteContent(): {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(WorkflowResponse.builder().status(500).message("An unexpected error occurred: " + e.getMessage()).build());
        }
    }

    @DeleteMapping("/content/subactivity/{subActivityId}")
    public ResponseEntity<?> deleteBySubActivityId(@PathVariable Long subActivityId) {
        try {
            service.deleteBySubActivityId(subActivityId);
            log.info("Content deleted successfully for subActivityId={}", subActivityId);
            return ResponseEntity.ok(WorkflowResponse.builder().status(200).message("Deleted successfully").build());
        } catch (IllegalArgumentException e) {
            log.error("Invalid request in deleteBySubActivityId(): {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(WorkflowResponse.builder().status(400).message("Invalid request: " + e.getMessage()).build());
        } catch (Exception e) {
            log.error("Unexpected error in deleteBySubActivityId(): {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder().status(500).message("An unexpected error occurred: " + e.getMessage()).build());
        }
    }

    @PutMapping("content/save/remarks/")
    public ResponseEntity<?> addingRemarksToResourceExpenditure(Principal principal, @RequestBody NonTrainingExpenditureRemarksDTO remarksDTO, @RequestParam(value = "status", required = false) BillRemarksStatus status, HttpServletRequest servletRequest) {
        try {
            log.info("Remarks added/updated successfully for NIMSME Content");
            return ResponseEntity.ok(service.addRemarkOrResponse(remarksDTO, status));
        } catch (DataException e) {
            log.error("DataException in addingRemarksToResourceExpenditure(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }
}
