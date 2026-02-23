package com.metaverse.workflow.ramp.service;

import com.metaverse.workflow.common.response.WorkflowResponse;

public interface RampService {

    WorkflowResponse saveEnrollment(RampEnrollmentRequest request);

    WorkflowResponse saveRegistration(RampRegistrationRequest request);
}

