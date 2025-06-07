package com.metaverse.workflow.organization.controller;

import com.metaverse.workflow.agency.service.AgencyResponseMapper;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.resouce.service.ResourceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.organization.service.OrganizationRequest;
import com.metaverse.workflow.organization.service.OrganizationResponse;
import com.metaverse.workflow.organization.service.OrganizationService;

import java.util.List;

@RestController
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping("/organization/save")
    public ResponseEntity<WorkflowResponse> saveOrganization(@RequestBody OrganizationRequest request) {
        WorkflowResponse response = organizationService.saveOrganization(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/organization/list")
    public ResponseEntity<WorkflowResponse> getResourcesByAgencyId() {
        WorkflowResponse response = organizationService.getOrganizations();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/organization/mobileno/exist/{mobileNo}")
    public ResponseEntity<Boolean> isParticipantsMobileNoExist(@PathVariable Long mobileNo) {
        Boolean mobileNumberExists = organizationService.isMobileNumberExists(mobileNo);
        return ResponseEntity.ok(mobileNumberExists);
    }

}
