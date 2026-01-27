package com.metaverse.workflow.organization.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.nontrainingExpenditures.controller.WeHubController;
import com.metaverse.workflow.organization.service.OrganizationRequest;
import com.metaverse.workflow.organization.service.OrganizationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final static Logger log = LogManager.getLogger(OrganizationController.class);


    @PostMapping("/organization/save")
    public ResponseEntity<WorkflowResponse> saveOrganization(@RequestBody OrganizationRequest request, Principal principal) {
        WorkflowResponse response = organizationService.saveOrganization(request);
        logService.logs(principal.getName(), "SAVE", "organization creation", "organization", "/organization/save");
        log.info(principal.getName(), "SAVE", "organization update", "organization", "/organization/save");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/organization/list")
    public ResponseEntity<WorkflowResponse> getResourcesByAgencyId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String orgType) {
        return ResponseEntity.ok(organizationService.getOrganizations(page, size, orgType));
    }

    //    @GetMapping("/organization-names")
//    public ResponseEntity<WorkflowResponse> getAllOrganizations() {
//        WorkflowResponse allOrganizations  = organizationService.getAllOrganizations();
//        return ResponseEntity.ok(allOrganizations);
//    }
    @GetMapping("/organization-names")
    public ResponseEntity<WorkflowResponse> getAllOrganizations(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String search
    ) {
        if (page == null && size == null && search == null) {
            return ResponseEntity.ok(organizationService.getAllOrganizations());
        }
        return ResponseEntity.ok(organizationService.getAllOrganizations(page, size, search));
    }



    @GetMapping("/organization/mobileno/exist/{mobileNo}")
    public ResponseEntity<Boolean> isParticipantsMobileNoExist(@PathVariable Long mobileNo) {
        Boolean mobileNumberExists = organizationService.isMobileNumberExists(mobileNo);
        return ResponseEntity.ok(mobileNumberExists);
    }

    @GetMapping("/organization{organizationId}")
    public ResponseEntity<WorkflowResponse> getOrganization(@PathVariable Long organizationId) {
        WorkflowResponse response = organizationService.getOrganizationbyId(organizationId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{organizationId}")
    public ResponseEntity<WorkflowResponse> patchOrganization(@PathVariable Long organizationId, @RequestBody OrganizationRequest request
    ) {
        WorkflowResponse updated = organizationService.updateOrganization(organizationId, request);
        log.info("Organization updated successfully");
        return ResponseEntity.ok(updated);
    }
}
