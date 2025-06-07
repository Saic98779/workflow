package com.metaverse.workflow.districtswithmandals.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.districtswithmandals.service.DistrictRequest;
import com.metaverse.workflow.districtswithmandals.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DistrictController {
    @Autowired
    private DistrictService districtService;
    @PostMapping("/district/save")
    public WorkflowResponse saveDistrict(@RequestBody DistrictRequest districtRequest)
    {
        return districtService.saveDistrict(districtRequest);
    }
    @GetMapping("/getAllDistricts")
    public WorkflowResponse getAllDistrict()
    {
        return districtService.getAllDistricts();
    }
    @GetMapping("/districtsById/{id}")
    public WorkflowResponse getAllDistrict(@PathVariable Integer id)
    {
        return districtService.getDistrictById(id);
    }
    @GetMapping("/getAllmandalsOfDistrictsById/{id}")
    public WorkflowResponse getAllMandalOfDistrict(@PathVariable Integer id)
    {
        return districtService.getAllMandalOfDistrict(id);
    }
    @GetMapping("/getAllmandalsOfDistrictsByName/{name}")
    public WorkflowResponse getAllMandalOfDistrict(@PathVariable String name)
    {
        return districtService.getAllMandalOfDistrictName(name);
    }
    @GetMapping("/gram/panchayth/mandal/id/{mandalId}")
    public ResponseEntity<WorkflowResponse> getAllGramPanchyatByMandalId(@PathVariable Integer mandalId)
    {
        WorkflowResponse response = districtService.getAllPanchayatByMandalId(mandalId);
        return ResponseEntity.ok(response);
    }
}
