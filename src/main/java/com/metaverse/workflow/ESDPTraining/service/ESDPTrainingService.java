package com.metaverse.workflow.ESDPTraining.service;

import com.metaverse.workflow.common.response.WorkflowResponse;

public interface ESDPTrainingService {
    WorkflowResponse saveESDPTrainingProgram(ESDPTrainingRequest esdpTrainingRequest);
    WorkflowResponse getESDPTrainingProgramData();
}
