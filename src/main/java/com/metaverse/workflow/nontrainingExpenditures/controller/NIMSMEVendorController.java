package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NIMSMEVendorDetailsDto;
import com.metaverse.workflow.nontrainingExpenditures.service.NIMSMEVendorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/vendors")
public class NIMSMEVendorController {

    @Autowired
    private NIMSMEVendorDetailsService service;

    @GetMapping(path = "/get-all")
    public ResponseEntity getAllVendors() {
        return ResponseEntity.ok(WorkflowResponse.builder().data(service.getAllVendors()).status(200).message("fetched successfully").build());
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<?> getVendorById(@PathVariable Long vendorId) {
        NIMSMEVendorDetailsDto vendorById = service.getVendorById(vendorId);
        if (vendorById != null) {
            return ResponseEntity.ok(WorkflowResponse.builder().data(service.getVendorById(vendorId)).status(200).message("fetched successfully").build());
        } else {
            return ResponseEntity.ofNullable(WorkflowResponse.builder().data("Id does not find").status(400).message("failed").build());
        }
    }

    @GetMapping("get/{subActivityId}")
    public ResponseEntity<?> getSubActivityById(@PathVariable Long subActivityId) {
        return ResponseEntity.ok(WorkflowResponse.builder().data(service.getSubActivityIdById(subActivityId)).status(200).message("fetched successfully").build());
    }

    @PostMapping(path = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WorkflowResponse> createVendor(@RequestPart("vendorDetailsDto")String vendorDetailsDto,
                                                         @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NIMSMEVendorDetailsDto dto = objectMapper.readValue(vendorDetailsDto, NIMSMEVendorDetailsDto.class);
            NIMSMEVendorDetailsDto nimsmeVendorDetailsDto = service.saveVendor(dto,file);
            return ResponseEntity.ok(
                    WorkflowResponse.builder()
                            .data(nimsmeVendorDetailsDto).status(201).message("Saved successfully").build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder().status(500).message("An unexpected error occurred: " + e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping(path = "/{vendorId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WorkflowResponse> updateVendor(@PathVariable Long vendorId, @RequestPart String vendorDetails,
                                                         @RequestPart(value = "files", required = false) MultipartFile file) {
        try {
            ObjectMapper om = new ObjectMapper();
           NIMSMEVendorDetailsDto dto = om.readValue(vendorDetails,NIMSMEVendorDetailsDto.class);
            NIMSMEVendorDetailsDto nimsmeVendorDetailsDto = service.updateVendor(vendorId, dto,file);
            return ResponseEntity.ok(
                    WorkflowResponse.builder().data(nimsmeVendorDetailsDto).status(200).message("Updated successfully")
                            .build());
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder()
                            .message("FAILURE: Invalid JSON format — " + e.getOriginalMessage()).status(400).build());

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder()
                            .message("FAILURE: File operation failed — " + e.getMessage()).status(500).build());

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder()
                            .message("FAILURE: " + e.getMessage()).status(400).build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder().message("FAILURE: Unexpected server error — " + e.getMessage()).status(500).build());
        }
    }

    @DeleteMapping("/{vendorId}")
    public ResponseEntity<?> deleteVendor(@PathVariable Long vendorId) {

        try {
            return ResponseEntity.ok(service.deleteVendor(vendorId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder().status(500).message("An unexpected error occurred: " + e.getMessage())
                            .build()
            );
        }

    }

    @DeleteMapping("/subactivity/{subActivityId}")
    public ResponseEntity<?> deleteVendorsBySubActivity(@PathVariable Long subActivityId) {
        try {
            service.deleteBySubActivityId(subActivityId);
            return ResponseEntity.ok(WorkflowResponse.builder().status(200).message("Deleted successfully").build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder().status(500).message("An unexpected error occurred: " + e.getMessage())
                            .build()
            );
        }
    }
}
