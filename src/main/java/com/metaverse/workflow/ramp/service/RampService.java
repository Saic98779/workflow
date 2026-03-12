package com.metaverse.workflow.ramp.service;

import com.metaverse.workflow.common.response.WorkflowResponse;

import java.util.List;

public interface RampService {

    WorkflowResponse saveEnrollment(RampEnrollmentRequest request);

    WorkflowResponse saveRegistration(RampRegistrationRequest request);

    List<DistrictProgramReport> getDistrictWiseReport();
}

