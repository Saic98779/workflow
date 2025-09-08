package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubHandholdingRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubSDGRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubSelectedCompaniesRequest;

public interface WeHubService {
    WorkflowResponse create(WeHubSelectedCompaniesRequest request) throws DataException;
    WorkflowResponse update(Long candidateId, WeHubSelectedCompaniesRequest request) throws DataException;
    WorkflowResponse getBySubActivityId(Long subActivityId) throws DataException;
    WorkflowResponse delete(Long candidateId) throws DataException;
    WorkflowResponse getById(Long candidateId) throws DataException;
    WorkflowResponse getSelectedOrganization();

    WorkflowResponse createHandholding(WeHubHandholdingRequest request) throws DataException;
    WorkflowResponse updateHandholding(Long handholdingId, WeHubHandholdingRequest request) throws DataException;
    WorkflowResponse getHandholdingBySubActivityId(Long subActivityId) throws DataException;
    WorkflowResponse deleteHandholding(Long handholdingId) throws DataException;
    WorkflowResponse getHandholdingById(Long handholdingId) throws DataException;

    WorkflowResponse createWeHubSDG(WeHubSDGRequest request) throws DataException;
    WorkflowResponse updateWeHubSDG(Long eeHubSDGId, WeHubSDGRequest request) throws DataException;
    WorkflowResponse getWeHubSDGByActivityId(Long nonTrainingActivityId) throws DataException;
    WorkflowResponse deleteWeHubSDG(Long weHubSDGId) throws DataException;
    WorkflowResponse getWeHubSDGById(Long weHubSDGId) throws DataException;


}