package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubSelectedCompaniesRequest;

public interface WeHubService {
    WorkflowResponse create(WeHubSelectedCompaniesRequest request) throws DataException;
    WorkflowResponse update(Long candidateId, WeHubSelectedCompaniesRequest request) throws DataException;
    WorkflowResponse getBySubActivityId(Long subActivityId) throws DataException;
    WorkflowResponse delete(Long candidateId) throws DataException;
    WorkflowResponse getById(Long candidateId) throws DataException;
}