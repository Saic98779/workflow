package com.metaverse.workflow.programoutcometargets.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.programoutcometargets.dto.FinancialTargetOverAllDTO;
import com.metaverse.workflow.programoutcometargets.dto.FinancialTargetSummaryDTO;

import java.util.List;

public interface TargetService {
    WorkflowResponse saveFinancialTarget(FinancialTargetRequest request) throws DataException;
    WorkflowResponse updateFinancialTarget(FinancialTargetRequest request, Long financialTargetId) throws DataException;
    WorkflowResponse getFinancialTargetsById(Long financialTargetId) throws DataException;
    WorkflowResponse getFinancialTargetsByAgencyId(Long agencyId) throws DataException;
    WorkflowResponse deleteFinancialTarget(Long financialTargetId)throws DataException;

    WorkflowResponse savePhysicalTarget(PhysicalTargetRequest request) throws DataException;
    WorkflowResponse updatePhysicalTarget(PhysicalTargetRequest request, Long financialTargetId) throws DataException;
    WorkflowResponse getPhysicalTargetsById(Long financialTargetId) throws DataException;
    WorkflowResponse getPhysicalTargetsByAgencyId(Long agencyId) throws DataException;
    WorkflowResponse deletePhysicalTarget(Long financialTargetId)throws DataException;
    FinancialTargetOverAllDTO getFinancialTargetSummary(Long agencyId);
    }
