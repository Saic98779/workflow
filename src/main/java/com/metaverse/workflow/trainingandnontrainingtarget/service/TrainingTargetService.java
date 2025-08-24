package com.metaverse.workflow.trainingandnontrainingtarget.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import org.springframework.stereotype.Service;

@Service
public interface TrainingTargetService {
    WorkflowResponse getTrainingTargetsByAgencyId(Long agencyId) throws DataException;

}
