package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NIMSMEVendorDetailsDto;
import com.metaverse.workflow.nontrainingExpenditures.service.NIMSMEVendorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path = "/save")
    public ResponseEntity<WorkflowResponse> createVendor(@RequestBody NIMSMEVendorDetailsDto vendorDetailsDto) {
        try {
            NIMSMEVendorDetailsDto nimsmeVendorDetailsDto = service.saveVendor(vendorDetailsDto);
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

    @PutMapping("/{vendorId}")
    public ResponseEntity<WorkflowResponse> updateVendor(@PathVariable Long vendorId, @RequestBody NIMSMEVendorDetailsDto vendorDetails) {
        try {
            NIMSMEVendorDetailsDto nimsmeVendorDetailsDto = service.updateVendor(vendorId, vendorDetails);
            return ResponseEntity.ok(
                    WorkflowResponse.builder().data(nimsmeVendorDetailsDto).status(200).message("Updated successfully")
                            .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder().status(500).message("An unexpected error occurred: " + e.getMessage())
                            .build()
            );
        }
    }


    @DeleteMapping("/{vendorId}")
    public ResponseEntity<?> deleteVendor(@PathVariable Long vendorId) {

        try {
            service.deleteVendor(vendorId);
            return ResponseEntity.ok(WorkflowResponse.builder().status(200).message("Deleted successfully").build());
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
