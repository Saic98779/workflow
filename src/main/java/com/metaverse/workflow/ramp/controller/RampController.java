package com.metaverse.workflow.ramp.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.ramp.service.DistrictProgramReport;
import com.metaverse.workflow.ramp.service.RampEnrollmentRequest;
import com.metaverse.workflow.ramp.service.RampRegistrationRequest;
import com.metaverse.workflow.ramp.service.RampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ramp")
public class RampController {

    @Autowired
    private RampService rampService;

    @PostMapping("/enrollments")
    public ResponseEntity<WorkflowResponse> saveEnrollment(@RequestBody RampEnrollmentRequest request) {
        WorkflowResponse response = rampService.saveEnrollment(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registrations")
    public ResponseEntity<WorkflowResponse> saveRegistration(@RequestBody RampRegistrationRequest request) {
        WorkflowResponse response = rampService.saveRegistration(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/district-wise")
    public ResponseEntity<List<DistrictProgramReport>> getDistrictWiseReport() {
        return ResponseEntity.ok(rampService.getDistrictWiseReport());
    }
}

