package com.metaverse.workflow.counsellor.service;

import com.metaverse.workflow.common.response.WorkflowResponse;

public interface CounsellorService {
   WorkflowResponse saveCounseller(CounsellorRegistrationRequest counsellorRequest);
    WorkflowResponse getCounsellerByMandal(Integer mandalId);

    WorkflowResponse getAllCounsellors();
}
