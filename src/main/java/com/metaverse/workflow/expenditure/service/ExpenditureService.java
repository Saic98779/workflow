package com.metaverse.workflow.expenditure.service;

import com.metaverse.workflow.common.enums.ExpenditureType;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.*;
import com.metaverse.workflow.model.HeadOfExpense;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExpenditureService {

    
    WorkflowResponse saveBulkExpenditure(BulkExpenditureRequest expenditureRequest, List<MultipartFile> multipartFiles) throws DataException;
    WorkflowResponse saveProgramExpenditure(ProgramExpenditureRequest expenditureRequest, List<MultipartFile> files) throws DataException;
    BulkExpenditureTransactionResponse saveTransaction(BulkExpenditureTransactionRequest request) throws DataException;
    WorkflowResponse getAllBulkExpenditure();
    WorkflowResponse getAllProgramExpenditure(ExpenditureType expenditureType);
    WorkflowResponse getAllProgramExpenditureByProgram(ExpenditureType expenditureType,Long programId);
    WorkflowResponse getAllBulkExpenditureTransactionByProgram(Long programId);
    BulkExpenditureLookupResponse getBulkExpendituresByExpenseAndItem(BulkExpenditureLookupRequest request) throws DataException;
    List<String> getItemsByHeadOfExpense(Integer expenseId) throws DataException;
    List<HeadOfExpense> getAllHeadOfExpenses();
    WorkflowResponse updateProgramExpenditure(Long expenditureId, ProgramExpenditureRequest expenditureRequest, List<MultipartFile> files) throws DataException;
    WorkflowResponse deleteProgramExpenditure(Long expenditureId) throws DataException;
    WorkflowResponse updateBulkExpenditure(Long expenditureId, BulkExpenditureRequest bulkExpenditureRequest, List<MultipartFile> files) throws DataException;
    WorkflowResponse deleteBulkExpenditure(Long expenditureId) throws DataException;
    WorkflowResponse updateTransaction(Long transactionId, BulkExpenditureTransactionRequest request) throws DataException;
    WorkflowResponse getAllBulkExpenditureByAgencyId(Long agencyId);
    WorkflowResponse getAllProgramExpenditureByProgramIdByAgencyId(ExpenditureType expenditureType, Long agencyId,Long programId);
    WorkflowResponse deleteTransaction(Long transactionId) throws DataException;
    List<ProgramExpenditureResponse> getAllProgramExpenditure(Long agencyId, Long programId);

}

