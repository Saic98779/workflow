package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NIMSMEVendorDetailsDto;
import com.metaverse.workflow.nontrainingExpenditures.service.NIMSMEVendorDetailsService;
import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingExpenditureRemarksDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/vendors")
@Tag(name = "Non Training", description = "NIMSME Vendor Details APIs")
public class NIMSMEVendorController {

    private static final Logger log = LogManager.getLogger(NIMSMEVendorController.class);

    @Autowired
    private NIMSMEVendorDetailsService service;

    @GetMapping(path = "/get-all")
    public ResponseEntity getAllVendors() {
        ResponseEntity response = ResponseEntity.ok(WorkflowResponse.builder().data(service.getAllVendors()).status(200).message("fetched successfully").build());
        log.info("Fetched all vendors successfully");
        return response;
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<?> getVendorById(@PathVariable Long vendorId) {
        NIMSMEVendorDetailsDto vendorById = service.getVendorById(vendorId);
        if (vendorById != null) {
            log.info("Vendor fetched successfully for vendorId={}", vendorId);
            return ResponseEntity.ok(WorkflowResponse.builder().data(service.getVendorById(vendorId)).status(200).message("fetched successfully").build());
        } else {
            log.warn("Vendor not found for vendorId={}", vendorId);
            return ResponseEntity.ofNullable(WorkflowResponse.builder().data("Id does not find").status(400).message("failed").build()
            );
        }
    }

    @GetMapping("get/{subActivityId}")
    public ResponseEntity<?> getSubActivityById(@PathVariable Long subActivityId) {
        ResponseEntity<?> response =
                ResponseEntity.ok(WorkflowResponse.builder().data(service.getSubActivityIdById(subActivityId)).status(200).message("fetched successfully").build());
        log.info("Fetched vendors for subActivityId={}", subActivityId);
        return response;
    }

    @PostMapping(path = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WorkflowResponse> createVendor(@RequestPart("vendorDetailsDto")String vendorDetailsDto,
                                                         @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NIMSMEVendorDetailsDto dto = objectMapper.readValue(vendorDetailsDto, NIMSMEVendorDetailsDto.class);
            NIMSMEVendorDetailsDto nimsmeVendorDetailsDto = service.saveVendor(dto,file);
            log.info("Vendor saved successfully");
            return ResponseEntity.ok(
                    WorkflowResponse.builder().data(nimsmeVendorDetailsDto).status(201).message("Saved successfully").build());

        } catch (Exception e) {
            log.error("Error in createVendor(): {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder()
                            .status(500)
                            .message("An unexpected error occurred: " + e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping(path = "/{vendorId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WorkflowResponse> updateVendor(@PathVariable Long vendorId, @RequestPart String vendorDetails,
                                                         @RequestPart(value = "files", required = false) MultipartFile file) {
        try {
            ObjectMapper om = new ObjectMapper();
            NIMSMEVendorDetailsDto dto = om.readValue(vendorDetails, NIMSMEVendorDetailsDto.class);

            NIMSMEVendorDetailsDto updated = service.updateVendor(vendorId, dto, file);

            log.info("Vendor updated successfully for vendorId={}", vendorId);

            return ResponseEntity.ok(WorkflowResponse.builder().data(updated).status(200).message("Updated successfully").build());

        } catch (JsonProcessingException e) {
            log.error("Invalid JSON in updateVendor(): {}", e.getOriginalMessage(), e);
            return ResponseEntity.badRequest().body(WorkflowResponse.builder().message("FAILURE: Invalid JSON format — " + e.getOriginalMessage()).status(400).build());

        } catch (IOException e) {
            log.error("File error in updateVendor(): {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder().message("FAILURE: File operation failed — " + e.getMessage()).status(500).build());

        } catch (RuntimeException e) {
            log.error("Runtime error in updateVendor(): {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder().message("FAILURE: " + e.getMessage()).status(400).build());

        } catch (Exception e) {
            log.error("Unexpected error in updateVendor(): {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(WorkflowResponse.builder().message("FAILURE: Unexpected server error — " + e.getMessage()).status(500).build());
        }
    }

    @DeleteMapping("/{vendorId}")
    public ResponseEntity<?> deleteVendor(@PathVariable Long vendorId) {
        try {
            ResponseEntity<?> response = ResponseEntity.ok(service.deleteVendor(vendorId));
            log.info("Vendor deleted successfully for vendorId={}", vendorId);
            return response;
        } catch (Exception e) {
            log.error("Error in deleteVendor(): {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(WorkflowResponse.builder().status(500).message("An unexpected error occurred: " + e.getMessage()).build());
        }
    }

    @DeleteMapping("/subactivity/{subActivityId}")
    public ResponseEntity<?> deleteVendorsBySubActivity(@PathVariable Long subActivityId) {
        try {
            service.deleteBySubActivityId(subActivityId);
            log.info("Vendors deleted successfully for subActivityId={}", subActivityId);
            return ResponseEntity.ok(WorkflowResponse.builder().status(200).message("Deleted successfully").build());
        } catch (Exception e) {
            log.error("Error in deleteVendorsBySubActivity(): {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(WorkflowResponse.builder().status(500).message("An unexpected error occurred: " + e.getMessage()).build());
        }
    }

    @PutMapping("/save/remarks")
    public ResponseEntity<?> addingRemarks(Principal principal, @RequestBody NonTrainingExpenditureRemarksDTO remarksDTO,
                                           @RequestParam(value = "status", required = false) BillRemarksStatus status,
                                           HttpServletRequest servletRequest) {
        try {
            log.info("Remarks added/updated successfully for Vendor");
            return  ResponseEntity.ok(service.addRemarkOrResponse(remarksDTO, status));
        } catch (DataException e) {
            log.error("DataException in addingRemarks(): {}", e.getMessage(), e);
            return RestControllerBase.error(e);
        }
    }
}
