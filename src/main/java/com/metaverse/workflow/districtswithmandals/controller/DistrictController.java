package com.metaverse.workflow.districtswithmandals.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.districtswithmandals.service.DistrictRequest;
import com.metaverse.workflow.districtswithmandals.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DistrictController {
    private static final Logger log = LogManager.getLogger(DistrictController.class);
    @Autowired
    private DistrictService districtService;
    @PostMapping("/district/save")
    public WorkflowResponse saveDistrict(@RequestBody DistrictRequest districtRequest) {

        WorkflowResponse response = districtService.saveDistrict(districtRequest);
        log.info("District saved successfully");

        return response;
    }
    @GetMapping("/getAllDistricts")
    public WorkflowResponse getAllDistricts() {

        WorkflowResponse response = districtService.getAllDistricts();
        log.info("Fetched all districts successfully");

        return response;
    }
    @GetMapping("/districtsById/{id}")
    public WorkflowResponse getDistrictById(@PathVariable Integer id) {

        WorkflowResponse response = districtService.getDistrictById(id);
        log.info("Fetched district for id={}", id);

        return response;
    }
    @GetMapping("/getAllmandalsOfDistrictsById/{id}")
    public WorkflowResponse getAllMandalOfDistrict(@PathVariable Integer id) {
        WorkflowResponse response = districtService.getAllMandalOfDistrict(id);
        log.info("Fetched mandals for districtId={}", id);
        return response;
    }
    @GetMapping("/getAllmandalsOfDistrictsByName/{name}")
    public WorkflowResponse getAllMandalOfDistrictByName(@PathVariable String name) {
        WorkflowResponse response = districtService.getAllMandalOfDistrictName(name);
        log.info("Fetched mandals for districtName={}", name);
        return response;
    }
    @GetMapping("/gram/panchayth/mandal/id/{mandalId}")
    public ResponseEntity<WorkflowResponse> getAllGramPanchyatByMandalId(@PathVariable Integer mandalId) {
        WorkflowResponse response = districtService.getAllPanchayatByMandalId(mandalId);
        log.info("Fetched gram panchayats for mandalId={}", mandalId);
        return ResponseEntity.ok(response);
    }
}
