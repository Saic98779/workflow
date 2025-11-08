package com.metaverse.workflow.organization.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.organization.service.OrganizationRequest;
import com.metaverse.workflow.organization.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ActivityLogService logService;

    @PostMapping("/organization/save")
    public ResponseEntity<WorkflowResponse> saveOrganization(@RequestBody OrganizationRequest request, Principal principal) {
        WorkflowResponse response = organizationService.saveOrganization(request);
        logService.logs(principal.getName(), "SAVE","organization creation","organization","/organization/save");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/organization/list")
    public ResponseEntity<WorkflowResponse> getResourcesByAgencyId(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
            return ResponseEntity.ok(organizationService.getOrganizations(page, size));
    }

    @GetMapping("/organization-names")
    public ResponseEntity<WorkflowResponse> getAllOrganizations() {
        WorkflowResponse allOrganizations  = organizationService.getAllOrganizations();
        return ResponseEntity.ok(allOrganizations);
    }

    @GetMapping("/organization/mobileno/exist/{mobileNo}")
    public ResponseEntity<Boolean> isParticipantsMobileNoExist(@PathVariable Long mobileNo) {
        Boolean mobileNumberExists = organizationService.isMobileNumberExists(mobileNo);
        return ResponseEntity.ok(mobileNumberExists);
    }

}
