package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NIMSMEContentDetailsDto;
import com.metaverse.workflow.nontrainingExpenditures.service.NIMSMEContentDetailsService;
import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingExpenditureRemarksDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/central-data")
public class NIMSMEContentDetailsController {

    @Autowired
    private NIMSMEContentDetailsService service;

    @PostMapping(path = "/content/save")
    public ResponseEntity<?> createContent(@RequestBody NIMSMEContentDetailsDto nimsmeContentDetailsDto) {
        return ResponseEntity.ok(WorkflowResponse.builder().data(service.saveContent(nimsmeContentDetailsDto)).status(201).message("Saved successfully").build());
    }

    @GetMapping(path = "/content/get-all")
    public ResponseEntity<?> getAllContent() {
        return ResponseEntity.ok(WorkflowResponse.builder().data(service.getAllContent()).status(200).message("fetched successfully").build());
    }

    @GetMapping("/content/{contentId}")
    public ResponseEntity<?> getContentById(@PathVariable Long contentId) {
        try {
            NIMSMEContentDetailsDto data = service.getContentById(contentId);
            return ResponseEntity.ok(
                    WorkflowResponse.builder()
                            .data(data).status(200).message("Fetched successfully")
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(WorkflowResponse.builder()
                            .status(404).message(e.getMessage())
                            .build());
        }
    }

    @GetMapping("/content/subActivity/{nonTrainingSubActivityId}")
    public ResponseEntity<?> getContentByNonTrainingSubActivityId(@PathVariable Long nonTrainingSubActivityId) {
        try {
            List<NIMSMEContentDetailsDto> data = service.getContentByNonTrainingSubActivityId(nonTrainingSubActivityId);
            return ResponseEntity.ok(
                    WorkflowResponse.builder()
                            .data(data).status(200).message("Fetched successfully")
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(WorkflowResponse.builder().status(404).message(e.getMessage())
                            .build());
        }
    }


    @PutMapping("/content/{contentId}")
    public ResponseEntity<?> updateContent(@PathVariable Long contentId, @RequestBody NIMSMEContentDetailsDto nimsmeContentDetailsDto) {

        try {
            NIMSMEContentDetailsDto nimsmeContentDetailsDto1 = service.updateContent(contentId, nimsmeContentDetailsDto);
            return ResponseEntity.ok(
                    WorkflowResponse.builder()
                            .data(nimsmeContentDetailsDto1).status(200).message("Content updated successfully").build()
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder().status(400).message("Invalid request: " + e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder()
                            .status(500).message("An unexpected error occurred: " + e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/content/{contentId}")
    public ResponseEntity<?> deleteContent(@PathVariable Long contentId) {
        try {
            service.deleteContent(contentId);
            return ResponseEntity.ok(WorkflowResponse.builder().status(200).message("Deleted successfully").build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder().status(400).message("Invalid request: " + e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder().status(500).message("An unexpected error occurred: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/content/subactivity/{subActivityId}")
    public ResponseEntity<?> deleteBySubActivityId(@PathVariable Long subActivityId) {
        try {
            service.deleteBySubActivityId(subActivityId);
            return ResponseEntity.ok(WorkflowResponse.builder().status(200).message("Deleted successfully").build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder().status(400).message("Invalid request: " + e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder().status(500).message("An unexpected error occurred: " + e.getMessage())
                            .build());
        }
    }

    @PutMapping("content/save/remarks/")
    public ResponseEntity<?> addingRemarksToResourceExpenditure(Principal principal, @RequestBody NonTrainingExpenditureRemarksDTO remarksDTO,
                                                                @RequestParam(value = "status", required = false) BillRemarksStatus status,
                                                                HttpServletRequest servletRequest) {
        try {
            return  ResponseEntity.ok(service.addRemarkOrResponse(remarksDTO, status));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

}
