package com.metaverse.workflow.ProgramMonitoring.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;

public interface ProgramMonitoringService {
    WorkflowResponse saveFeedback(ProgramMonitoringRequest request) throws DataException;
    WorkflowResponse updateFeedback(Long monitorId, ProgramMonitoringRequest request) throws DataException;
    WorkflowResponse getFeedBackByProgramId(Long programId) throws DataException;
    WorkflowResponse getFeedBackById(Long feedBackId);
    WorkflowResponse getProgramDetailsFroFeedBack(Long programId) throws DataException;
}