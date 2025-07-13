package com.metaverse.workflow.expenditure.service;

import com.metaverse.workflow.activity.repository.ActivityRepository;
import com.metaverse.workflow.activity.repository.SubActivityRepository;
import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.enums.ExpenditureType;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.*;
import com.metaverse.workflow.expenditure.repository.BulkExpenditureRepository;
import com.metaverse.workflow.expenditure.repository.BulkExpenditureTransactionRepository;
import com.metaverse.workflow.expenditure.repository.HeadOfExpenseRepository;
import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.*;

import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import com.metaverse.workflow.program.service.ProgramServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenditureServiceAdepter implements ExpenditureService {
    @Autowired
    ProgramExpenditureRepository programExpenditureRepository;
    @Autowired
    BulkExpenditureRepository bulkExpenditureRepository;
    @Autowired
    private BulkExpenditureTransactionRepository transactionRepo;
    @Autowired
    AgencyRepository agencyRepository;
    @Autowired
    ProgramRepository programRepository;
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    SubActivityRepository subActivityRepository;
    @Autowired
    HeadOfExpenseRepository headOfExpenseRepository;
    @Autowired
    ProgramSessionFileRepository programSessionFileRepository;
    @Autowired
    ProgramServiceAdapter programServiceAdapter;
    @Autowired
    LoginRepository userRepo;

    @Override
    public WorkflowResponse saveBulkExpenditure(BulkExpenditureRequest expenditureRequest, List<MultipartFile> files) throws DataException {
        Agency agency = agencyRepository.findById(expenditureRequest.getAgencyId())
                .orElseThrow(() -> new DataException(
                        "Agency details for the agency id " + expenditureRequest.getAgencyId() + " do not exist.",
                        "AGENCY-DETAILS-NOT-FOUND",
                        400
                ));

        HeadOfExpense headOfExpense = headOfExpenseRepository.findById(expenditureRequest.getHeadOfExpenseId())
                .orElseThrow(() -> new DataException(
                        "HeadOfExpense details for the HeadOfExpense id " + expenditureRequest.getHeadOfExpenseId() + " do not exist.",
                        "HEAD-OF-EXPENSE-DETAILS-NOT-FOUND",
                        400
                ));

        boolean exists = bulkExpenditureRepository.existsByAgencyAndHeadOfExpenseAndItemName(
                agency,
                headOfExpense,
                expenditureRequest.getItemName()
        );

        if (exists) {
            throw new DataException("Same Item Name and Head of Expense already exists for your agency. Please change Item Name!", "BULK-EXPENDITURE_ALREADY-EXISTS", 400);
        }
        BulkExpenditure bulkExpenditure = bulkExpenditureRepository.save(
                ExpenditureRequestMapper
                        .mapBulkExpenditure(expenditureRequest, agency, headOfExpense)

        );
        if (files != null && !files.isEmpty()) {
            List<String> filePaths = programServiceAdapter.storageProgramFiles(files, expenditureRequest.getAgencyId(), "BulkExpenditure");
            List<ProgramSessionFile> sessionFiles = filePaths.stream()
                    .map(filePath -> ProgramSessionFile.builder()
                            .fileType("FILE")
                            .filePath(filePath)
                            .bulkExpenditure(bulkExpenditure)
                            .build())
                    .toList();
            programSessionFileRepository.saveAll(sessionFiles);

        }

        return WorkflowResponse.builder()
                .message("BulkExpenditure saved successfully")
                .data(ExpenditureResponseMapper.mapBulkExpenditure(
                        bulkExpenditure
                ))
                .status(200).build();
    }

    @Override
    public WorkflowResponse getAllBulkExpenditure() {
        List<BulkExpenditure> bulkExpenditureList = bulkExpenditureRepository.findAll();
        if (bulkExpenditureList.isEmpty())
            return WorkflowResponse.builder().message("BulkExpenditure is Empty").status(400).build();
        List<BulkExpenditureResponse> responses = bulkExpenditureList.stream()
                .map(bulkExpenditure -> {
                    List<Long> fileIds = programSessionFileRepository
                            .findByBulkExpenditureId(bulkExpenditure.getBulkExpenditureId())
                            .stream()
                            .map(ProgramSessionFile::getProgramSessionFileId)
                            .toList();
                    return ExpenditureResponseMapper.mapBulkExpenditure(bulkExpenditure, fileIds);
                })
                .toList();

        return WorkflowResponse.builder().message("Success").status(200)
                .data(responses)
                .build();
    }


    @Override
    public WorkflowResponse getAllProgramExpenditure(ExpenditureType expenditureType) {
        List<ProgramExpenditure> programExpendituresList = programExpenditureRepository.findByExpenditureType(expenditureType);
        if (programExpendituresList.isEmpty())
            return WorkflowResponse.builder().message("Expenditures is Empty").status(400).build();

        List<ProgramExpenditureResponse> responses = programExpendituresList.stream()
                .map(programExpenditure -> {
                    List<Long> fileIds = programSessionFileRepository
                            .findByBulkExpenditureId(programExpenditure.getProgramExpenditureId())
                            .stream()
                            .map(ProgramSessionFile::getProgramSessionFileId)
                            .toList();
                    return ExpenditureResponseMapper.mapProgramExpenditure(programExpenditure, fileIds);
                })
                .toList();
        return WorkflowResponse.builder().message("Success").status(200)
                .data(responses)
                .build();
    }

    @Override
    public WorkflowResponse getAllProgramExpenditureByProgramIdByAgencyId(ExpenditureType expenditureType, Long agencyId, Long programId) {
        List<ProgramExpenditure> programExpendituresList =
                programExpenditureRepository.findByExpenditureTypeAndAgency_AgencyIdAndProgram_ProgramId(expenditureType, agencyId, programId);
        if (programExpendituresList.isEmpty())
            return WorkflowResponse.builder().message("No BulkExpenditure found for the given agency ID").status(400).build();

        List<ProgramExpenditureResponse> responses = programExpendituresList.stream()
                .map(programExpenditure -> {
                    List<Long> fileIds = programSessionFileRepository
                            .findByBulkExpenditureId(programExpenditure.getProgramExpenditureId())
                            .stream()
                            .map(ProgramSessionFile::getProgramSessionFileId)
                            .toList();
                    return ExpenditureResponseMapper.mapProgramExpenditure(programExpenditure, fileIds);
                })
                .toList();
        return WorkflowResponse.builder().message("Success").status(200)
                .data(responses)
                .build();
    }

    @Override
    public List<ProgramExpenditureResponse> getAllProgramExpenditure(Long agencyId, Long programId) {
        List<ProgramExpenditure> programExpendituresList =
                programExpenditureRepository.findByAgency_AgencyIdAndProgram_ProgramId(agencyId, programId);

        List<ProgramExpenditureResponse> responses = programExpendituresList.stream()
                .map(programExpenditure -> {
                    List<Long> fileIds = programSessionFileRepository
                            .findByBulkExpenditureId(programExpenditure.getProgramExpenditureId())
                            .stream()
                            .map(ProgramSessionFile::getProgramSessionFileId)
                            .toList();
                    return ExpenditureResponseMapper.mapProgramExpenditure(programExpenditure, fileIds);
                })
                .toList();
        return responses;
    }


    @Override
    public WorkflowResponse getAllProgramExpenditureByProgram(ExpenditureType expenditureType, Long programId) {
        List<ProgramExpenditure> programExpendituresList = programExpenditureRepository.findByExpenditureTypeAndProgram_ProgramId(expenditureType, programId);
        if (programExpendituresList.isEmpty())
            return WorkflowResponse.builder().message("Expenditures is Empty").status(400).build();
        List<ProgramExpenditureResponse> responses = programExpendituresList.stream()
                .map(programExpenditure -> {
                    List<Long> fileIds = programSessionFileRepository
                            .findByBulkExpenditureId(programExpenditure.getProgramExpenditureId())
                            .stream()
                            .map(ProgramSessionFile::getProgramSessionFileId)
                            .toList();
                    return ExpenditureResponseMapper.mapProgramExpenditure(programExpenditure, fileIds);
                })
                .toList();
        return WorkflowResponse.builder().message("Success").status(200)
                .data(responses)
                .build();
    }

    @Override
    public BulkExpenditureTransactionResponse saveTransaction(BulkExpenditureTransactionRequest request) throws DataException {

        BulkExpenditure bulkExpenditure = bulkExpenditureRepository.findById(request.getBulkExpenditureId())
                .orElseThrow(() -> new DataException("Bulk expenditure data not found", "BULK-EXPENDITURE-DATA-NOT-FOUND", 400));
        Activity activity = activityRepository.findById(request.getActivityId())
                .orElseThrow(() -> new DataException("Activity data not found", "ACTIVITY-DATA-NOT-FOUND", 400));
        SubActivity subActivity = subActivityRepository.findById(request.getSubActivityId())
                .orElseThrow(() -> new DataException("Sub Activity data not found", "SUB-ACTIVITY-DATA-NOT-FOUND", 400));
        Program program = programRepository.findById(request.getProgramId())
                .orElseThrow(() -> new DataException("Program data not found", "PROGRAM-DATA-NOT-FOUND", 400));
        Agency agency = agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));
        HeadOfExpense headOfExpense = headOfExpenseRepository.findById(request.getHeadOfExpenseId())
                .orElseThrow(() -> new DataException("Head of expense data not found", "HEAD-OF-EXPENSE-DATA-NOT-FOUND", 400));

        BulkExpenditureTransaction saved = transactionRepo.save(ExpenditureRequestMapper.mapBulkExpenditureTransaction(request, activity, subActivity, program, agency, bulkExpenditure, headOfExpense));

        if (bulkExpenditure != null && request.getConsumedQuantity() != null) {
            int updatedAvailableQty = 0;
            if (bulkExpenditure.getAvailableQuantity() > request.getConsumedQuantity()) {
                updatedAvailableQty = bulkExpenditure.getAvailableQuantity() - request.getConsumedQuantity();
            }
            bulkExpenditure.setAvailableQuantity(updatedAvailableQty);
            bulkExpenditure.setConsumedQuantity(bulkExpenditure.getConsumedQuantity() + request.getConsumedQuantity());
            bulkExpenditureRepository.save(bulkExpenditure);
        }


        BulkExpenditureTransactionResponse response = BulkExpenditureTransactionResponse.builder()
                .id(saved.getBulkExpenditureTransactionId())
                .consumedQuantity(saved.getConsumedQuantity())
                .allocatedCost(saved.getAllocatedCost())
                .build();

        return response;
    }


    @Override
    public BulkExpenditureLookupResponse getBulkExpendituresByExpenseAndItem(BulkExpenditureLookupRequest request) throws DataException {
        HeadOfExpense headOfExpense = headOfExpenseRepository.findById(request.getExpenseId())
                .orElseThrow(() -> new DataException("Head of Expense not found", "HEAD-OF-EXPENSE-NOT-FOUND", 400));
        BulkExpenditure bulkExpenditure = bulkExpenditureRepository.findByHeadOfExpenseAndItemNameIgnoreCase(headOfExpense, request.getItemName());
        return ExpenditureResponseMapper.mapBulkExpenditureDetails(bulkExpenditure);
    }

    @Override
    public List<String> getItemsByHeadOfExpense(Integer expenseId) {
        return bulkExpenditureRepository.findDistinctItemNamesByHeadOfExpense(expenseId);
    }

    @Override
    public WorkflowResponse getAllBulkExpenditureTransactionByProgram(Long programId) {
        List<BulkExpenditureTransaction> transactions = transactionRepo.findByProgram_ProgramId(programId);
        transactions.stream().map(
                ExpenditureResponseMapper::mapBulkExpenditureTransaction).toList();

        return WorkflowResponse.builder().message("Success").status(200)
                .data(transactions.stream().map(
                        ExpenditureResponseMapper::mapBulkExpenditureTransaction).toList()
                )
                .build();
    }

    @Override
    public List<HeadOfExpense> getAllHeadOfExpenses() {
        return headOfExpenseRepository.findAll();
    }


    @Override
    public WorkflowResponse saveProgramExpenditure(ProgramExpenditureRequest expenditureRequest, List<MultipartFile> files) throws DataException {
        List<ProgramSessionFile> sessionFiles = new ArrayList<>();
                Program program = programRepository.findById(expenditureRequest.getProgramId())
                .orElseThrow(() -> new DataException(
                        "Program details for the program id " + expenditureRequest.getAgencyId() + " do not exist.",
                        "PROGRAM-DETAILS-NOT-FOUND",
                        400
                ));
        Agency agency = agencyRepository.findById(expenditureRequest.getAgencyId())
                .orElseThrow(() -> new DataException(
                        "Agency details for the agency id " + expenditureRequest.getAgencyId() + " do not exist.",
                        "AGENCY-DETAILS-NOT-FOUND",
                        400
                ));
        Activity activity = activityRepository.findById(expenditureRequest.getActivityId())
                .orElseThrow(() -> new DataException(
                        "Activity details for the activity id " + expenditureRequest.getAgencyId() + " do not exist.",
                        "ACTIVITY-DETAILS-NOT-FOUND",
                        400
                ));
        SubActivity subActivity = subActivityRepository.findById(expenditureRequest.getActivityId())
                .orElseThrow(() -> new DataException(
                        "SubActivity details for the subActivity id " + expenditureRequest.getAgencyId() + " do not exist.",
                        "SUB-ACTIVITY-DETAILS-NOT-FOUND",
                        400
                ));
        HeadOfExpense headOfExpense = headOfExpenseRepository.findById(expenditureRequest.getHeadOfExpenseId())
                .orElseThrow(() -> new DataException(
                        "HeadOfExpense details for the HeadOfExpense id " + expenditureRequest.getAgencyId() + " do not exist.",
                        "HEAD-OF-EXPENSE-DETAILS-NOT-FOUND",
                        400
                ));

        ProgramExpenditure programExpenditure = programExpenditureRepository.save(
                ExpenditureRequestMapper.mapProgramExpenditure(expenditureRequest, activity, subActivity, program, agency, headOfExpense));

        if (files != null && !files.isEmpty()) {
            List<String> filePaths = programServiceAdapter.storageProgramFiles(files, expenditureRequest.getProgramId(), "ProgramExpenditure");
            sessionFiles = filePaths.stream()
                    .map(filePath -> ProgramSessionFile.builder()
                            .fileType("FILE")
                            .filePath(filePath)
                            .programExpenditure(programExpenditure)
                            .build())
                    .toList();
            programSessionFileRepository.saveAll(sessionFiles);
        }
        if(!sessionFiles.isEmpty()) {
            programExpenditure.setUploadBillUrl(sessionFiles.get(0).getFilePath());
            programExpenditureRepository.save(programExpenditure);
        }

        return WorkflowResponse.builder()
                .message("Program Expenditure saved successfully")
                .data(ExpenditureResponseMapper.mapProgramExpenditure(programExpenditure
                ))
                .status(200).build();
    }

    @Override
    public WorkflowResponse updateProgramExpenditure(Long expenditureId, ProgramExpenditureRequest expenditureRequest, List<MultipartFile> files) throws DataException {
        ProgramExpenditure existingExpenditure = programExpenditureRepository.findById(expenditureId)
                .orElseThrow(() -> new DataException(
                        "Program Expenditure with ID " + expenditureId + " not found.",
                        "PROGRAM-EXPENDITURE-NOT-FOUND",
                        404
                ));

        Program program = programRepository.findById(expenditureRequest.getProgramId())
                .orElseThrow(() -> new DataException(
                        "Program details for ID " + expenditureRequest.getProgramId() + " do not exist.",
                        "PROGRAM-DETAILS-NOT-FOUND",
                        400
                ));

        Agency agency = agencyRepository.findById(expenditureRequest.getAgencyId())
                .orElseThrow(() -> new DataException(
                        "Agency details for ID " + expenditureRequest.getAgencyId() + " do not exist.",
                        "AGENCY-DETAILS-NOT-FOUND",
                        400
                ));

        Activity activity = activityRepository.findById(expenditureRequest.getActivityId())
                .orElseThrow(() -> new DataException(
                        "Activity details for ID " + expenditureRequest.getActivityId() + " do not exist.",
                        "ACTIVITY-DETAILS-NOT-FOUND",
                        400
                ));

        SubActivity subActivity = subActivityRepository.findById(expenditureRequest.getSubActivityId())
                .orElseThrow(() -> new DataException(
                        "SubActivity details for ID " + expenditureRequest.getSubActivityId() + " do not exist.",
                        "SUB-ACTIVITY-DETAILS-NOT-FOUND",
                        400
                ));

        HeadOfExpense headOfExpense = headOfExpenseRepository.findById(expenditureRequest.getHeadOfExpenseId())
                .orElseThrow(() -> new DataException(
                        "HeadOfExpense details for ID " + expenditureRequest.getHeadOfExpenseId() + " do not exist.",
                        "HEAD-OF-EXPENSE-DETAILS-NOT-FOUND",
                        400
                ));

        // Update fields
        ExpenditureRequestMapper.updateProgramExpenditure(existingExpenditure, expenditureRequest, activity, subActivity, program, agency, headOfExpense);
        ProgramExpenditure updatedExpenditure = programExpenditureRepository.save(existingExpenditure);

        List<ProgramSessionFile> oldFiles = programSessionFileRepository.findByProgramExpenditureId(expenditureId);
        if (!oldFiles.isEmpty()) {
            programSessionFileRepository.deleteAll(oldFiles);
        }
        // Handle files
        if (files != null && !files.isEmpty()) {
            List<String> filePaths = programServiceAdapter.storageProgramFiles(files, expenditureRequest.getProgramId(), "ProgramExpenditure");
            List<ProgramSessionFile> sessionFiles = filePaths.stream()
                    .map(filePath -> ProgramSessionFile.builder()
                            .fileType("FILE")
                            .filePath(filePath)
                            .programExpenditure(updatedExpenditure)
                            .build())
                    .toList();
            programSessionFileRepository.saveAll(sessionFiles);
            if(!sessionFiles.isEmpty()) {
                updatedExpenditure.setUploadBillUrl(sessionFiles.get(0).getFilePath());
                programExpenditureRepository.save(existingExpenditure);
            }
        }

        return WorkflowResponse.builder()
                .message("Program Expenditure updated successfully")
                .data(ExpenditureResponseMapper.mapProgramExpenditure(updatedExpenditure))
                .status(200)
                .build();
    }

    @Override
    public WorkflowResponse deleteProgramExpenditure(Long expenditureId) throws DataException {
        if (!programExpenditureRepository.existsById(expenditureId)) {
            throw new DataException(
                    "Program Expenditure with ID " + expenditureId + " not found.",
                    "PROGRAM-EXPENDITURE-NOT-FOUND",
                    404
            );
        }
        List<ProgramSessionFile> files = programSessionFileRepository.findByProgramExpenditureId(expenditureId);
        if (!files.isEmpty()) {
            programSessionFileRepository.deleteAll(files);
        }
        programExpenditureRepository.deleteById(expenditureId);

        return WorkflowResponse.builder()
                .status(200)
                .message("Program Expenditure deleted successfully")
                .data(null)
                .build();
    }

    @Override
    public WorkflowResponse updateBulkExpenditure(Long expenditureId, BulkExpenditureRequest expenditureRequest, List<MultipartFile> files) throws DataException {
        List<ProgramSessionFile> sessionFiles = new ArrayList<>();
        BulkExpenditure existingExpenditure = bulkExpenditureRepository.findById(expenditureId)
                .orElseThrow(() -> new DataException(
                        "BulkExpenditure with ID " + expenditureId + " does not exist.",
                        "BULK-EXPENDITURE-NOT-FOUND",
                        404
                ));

        Agency agency = agencyRepository.findById(expenditureRequest.getAgencyId())
                .orElseThrow(() -> new DataException(
                        "Agency details for the agency id " + expenditureRequest.getAgencyId() + " do not exist.",
                        "AGENCY-DETAILS-NOT-FOUND",
                        400
                ));

        HeadOfExpense headOfExpense = headOfExpenseRepository.findById(expenditureRequest.getHeadOfExpenseId())
                .orElseThrow(() -> new DataException(
                        "HeadOfExpense details for the HeadOfExpense id " + expenditureRequest.getHeadOfExpenseId() + " do not exist.",
                        "HEAD-OF-EXPENSE-DETAILS-NOT-FOUND",
                        400
                ));
        boolean exists = bulkExpenditureRepository.existsByAgencyAndHeadOfExpenseAndItemNameAndBulkExpenditureIdNot(agency, headOfExpense, expenditureRequest.getItemName(), expenditureId);
        if (exists) {
            throw new DataException(
                    "Same Item Name and Head of Expense already exists for your agency. Please change Item Name!",
                    "BULK-EXPENDITURE_ALREADY-EXISTS",
                    400
            );
        }
        ExpenditureRequestMapper.updateBulkExpenditure(existingExpenditure, expenditureRequest, agency, headOfExpense);

         BulkExpenditure bulkExpenditure = bulkExpenditureRepository.save(existingExpenditure);

        List<ProgramSessionFile> oldFiles = programSessionFileRepository.findByBulkExpenditureId(expenditureId);
        if (!oldFiles.isEmpty()) {
            programSessionFileRepository.deleteAll(oldFiles);

        }
        if (files != null && !files.isEmpty()) {
            List<String> filePaths = programServiceAdapter.storageProgramFiles(files, expenditureRequest.getAgencyId(), "BulkExpenditure");
            sessionFiles = filePaths.stream()
                    .map(filePath -> ProgramSessionFile.builder()
                            .fileType("FILE")
                            .filePath(filePath)
                            .bulkExpenditure(existingExpenditure)
                            .build())
                    .toList();
            programSessionFileRepository.saveAll(sessionFiles);
        }
        if(!sessionFiles.isEmpty()) {
            bulkExpenditure.setUploadBillUrl(sessionFiles.get(0).getFilePath());
            bulkExpenditureRepository.save(bulkExpenditure);
        }
        return WorkflowResponse.builder()
                .message("BulkExpenditure updated successfully")
                .data(ExpenditureResponseMapper.mapBulkExpenditure(existingExpenditure))
                .status(200)
                .build();
    }


    @Override
    @Transactional
    public WorkflowResponse deleteBulkExpenditure(Long expenditureId) throws DataException {
        if (!bulkExpenditureRepository.existsById(expenditureId)) {
            throw new DataException(
                    "Bulk Expenditure with ID " + expenditureId + " not found.",
                    "BULK-EXPENDITURE-NOT-FOUND",
                    404
            );
        }
        transactionRepo.deleteByExpenditureBulkExpenditureId(expenditureId);
        bulkExpenditureRepository.deleteById(expenditureId);
        List<ProgramSessionFile> file = programSessionFileRepository.findByBulkExpenditureId(expenditureId);
        if (!file.isEmpty()) {
            programSessionFileRepository.deleteAll(file);
        }

        return WorkflowResponse.builder()
                .status(200)
                .message("Bulk Expenditure deleted successfully")
                .data(null)
                .build();
    }

    @Override
    public WorkflowResponse updateTransaction(Long transactionId, BulkExpenditureTransactionRequest request) throws DataException {

        BulkExpenditureTransaction existingTransaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new DataException("Transaction not found", "TRANSACTION-NOT-FOUND", 404));
        BulkExpenditure bulkExpenditure = existingTransaction.getExpenditure();
        if (bulkExpenditure == null) {
            throw new DataException("Bulk expenditure data not linked", "BULK-EXPENDITURE-NOT-FOUND", 400);
        }
        Integer oldConsumedQty = existingTransaction.getConsumedQuantity();
        Integer newConsumedQty = request.getConsumedQuantity();

        int adjustedAvailableQty = bulkExpenditure.getAvailableQuantity() + oldConsumedQty - newConsumedQty;
        if (adjustedAvailableQty < 0) {
            throw new DataException("Not enough available quantity for update", "INSUFFICIENT-QUANTITY", 400);
        }

        bulkExpenditure.setAvailableQuantity(adjustedAvailableQty);
        bulkExpenditure.setConsumedQuantity(bulkExpenditure.getConsumedQuantity() - oldConsumedQty + newConsumedQty);
        bulkExpenditureRepository.save(bulkExpenditure);

        existingTransaction.setConsumedQuantity(newConsumedQty);
        existingTransaction.setAllocatedCost(request.getAllocatedCost());

        BulkExpenditureTransaction updated = transactionRepo.save(existingTransaction);

        BulkExpenditureTransactionResponse response = BulkExpenditureTransactionResponse.builder()
                .id(updated.getBulkExpenditureTransactionId())
                .consumedQuantity(updated.getConsumedQuantity())
                .allocatedCost(updated.getAllocatedCost())
                .build();

        return WorkflowResponse.builder()
                .status(200)
                .message("Transaction updated successfully")
                .data(response)
                .build();
    }

    @Override
    public WorkflowResponse getAllBulkExpenditureByAgencyId(Long agencyId) {
        List<BulkExpenditure> bulkExpenditureList = bulkExpenditureRepository.findByAgency_AgencyId(agencyId);
        if (bulkExpenditureList.isEmpty())
            return WorkflowResponse.builder().message("No BulkExpenditure found for the given agency ID").status(404).build();

        List<BulkExpenditureResponse> responses = bulkExpenditureList.stream()
                .map(bulkExpenditure -> {
                    List<Long> fileIds = programSessionFileRepository
                            .findByBulkExpenditureId(bulkExpenditure.getBulkExpenditureId())
                            .stream()
                            .map(ProgramSessionFile::getProgramSessionFileId)
                            .toList();
                    return ExpenditureResponseMapper.mapBulkExpenditure(bulkExpenditure, fileIds);
                })
                .toList();
        return WorkflowResponse.builder().message("Success").status(200)
                .data(responses)
                .build();
    }

    @Override
    public WorkflowResponse deleteTransaction(Long transactionId) throws DataException {
        BulkExpenditureTransaction transaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new DataException("Transaction not found", "TRANSACTION-NOT-FOUND", 400));

        BulkExpenditure bulkExpenditure = transaction.getExpenditure();
        if (bulkExpenditure == null) {
            throw new DataException("Associated Bulk Expenditure not found", "BULK-EXPENDITURE-NOT-FOUND", 400);
        }

        Integer consumedQty = transaction.getConsumedQuantity();
        if (consumedQty != null) {
            bulkExpenditure.setAvailableQuantity(bulkExpenditure.getAvailableQuantity() + consumedQty);
            bulkExpenditure.setConsumedQuantity(bulkExpenditure.getConsumedQuantity() - consumedQty);
            bulkExpenditureRepository.save(bulkExpenditure);
        }
        transactionRepo.delete(transaction);
        return WorkflowResponse.builder().message("Transaction Deleted Successfully..").status(200).build();
    }

    @Override
    public ExpenditureSummaryResponse getExpenditureHeadOfExpenseWise(Long programId) throws DataException {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new DataException("Program data not found", "PROGRAM-DATA-NOT-FOUND", 400));

        WorkflowResponse pre = getAllProgramExpenditureByProgram(ExpenditureType.PRE, program.getProgramId());
        WorkflowResponse post = getAllProgramExpenditureByProgram(ExpenditureType.POST, program.getProgramId());
        WorkflowResponse bulk = getAllBulkExpenditureTransactionByProgram(programId);

        if (pre.getData() == null || post.getData() == null || bulk.getData() == null) {
            throw new DataException("Missing expenditure data", "EXPENDITURE-DATA-NOT-FOUND", 400);
        }

        List<ProgramExpenditureResponse> expenditure = new ArrayList<>();
        expenditure.addAll((List<ProgramExpenditureResponse>) pre.getData());
        expenditure.addAll((List<ProgramExpenditureResponse>) post.getData());

        List<BulkTransactions> transactions = (List<BulkTransactions>) bulk.getData();

        List<CombinedExpenditure> combinedExpenditures = new ArrayList<>();
        for (ProgramExpenditureResponse exp : expenditure) {
            combinedExpenditures.add(
                    CombinedExpenditure.builder()
                            .headOfExpense(exp.getHeadOfExpense())
                            .expenditureType(exp.getExpenditureType())
                            .cost(exp.getCost() != null ? exp.getCost() : 0.0)
                            .billNo(exp.getBillNo())
                            .billDate(exp.getBillDate())
                            .payeeName(exp.getPayeeName())
                            .ifscCode(exp.getIfscCode())
                            .modeOfPayment(exp.getModeOfPayment())
                            .build()
            );
        }

        for (BulkTransactions exp : transactions) {
            combinedExpenditures.add(
                    CombinedExpenditure.builder()
                            .headOfExpense(exp.getHeadOfExpense())
                            .expenditureType(exp.getExpenditureType())
                            .cost(exp.getAllocatedCost() != null ? exp.getAllocatedCost() : 0.0)
                            .billNo(exp.getBillNo())
                            .billDate(exp.getBillDate() != null ? exp.getBillDate().toString().substring(0, 10) : null)
                            .payeeName(exp.getPayeeName())
                            .ifscCode(exp.getIfscCode())
                            .modeOfPayment(exp.getModeOfPayment())
                            .build()
            );
        }

        // Group by headOfExpense
        Map<String, List<CombinedExpenditure>> groupedByHead =
                combinedExpenditures.stream()
                        .filter(exp -> exp.getHeadOfExpense() != null)
                        .collect(Collectors.groupingBy(CombinedExpenditure::getHeadOfExpense));


        List<HeadWiseExpenditureSummary> summaries = groupedByHead.entrySet().stream()
                .map(e -> new HeadWiseExpenditureSummary(
                        e.getKey(),
                        e.getValue().stream().mapToDouble(c -> c.getCost() != null ? c.getCost() : 0.0).sum(),
                        e.getValue()
                ))
                .collect(Collectors.toList());

        double grandTotal = summaries.stream().mapToDouble(HeadWiseExpenditureSummary::getTotalCost).sum();

        return new ExpenditureSummaryResponse(summaries, grandTotal);
    }


    @Override
    public WorkflowResponse addRemarkOrResponse(ExpenditureRemarksDTO remarks) throws DataException {

        if (userRepo.existsById(remarks.getUserId())) {
            ProgramExpenditure exp = programExpenditureRepository.findById(remarks.getExpenditureId())
                    .orElseThrow(() -> new DataException("Expenditure not found", "EXPENDITURE_NOT_FOUND", 400));
           Optional<User> user= userRepo.findById(remarks.getUserId());
            ExpenditureRemarks remarkEntity = ExpenditureRemarksMapper.mapToEntity(remarks,user.get());
            remarkEntity.setExpenditure(exp);

            List<ExpenditureRemarks> remarksList = new ArrayList<>();
            remarksList.add(remarkEntity);

            exp.setRemarks(remarksList);

            programExpenditureRepository.save(exp);

        } else {
            throw new DataException("User not found for this id " + remarks.getUserId(), "USER_NOT_FOUND", 400);
        }

        return WorkflowResponse.builder()
                .message("Remark Or Response added Successfully.")
                .status(200)
                .build();
    }
    @Override
    public WorkflowResponse addRemarkOrResponseTransaction(ExpenditureRemarksDTO remarks) throws DataException {

        if (userRepo.existsById(remarks.getUserId())) {
            BulkExpenditureTransaction exp = transactionRepo.findById(remarks.getTransactionId())
                    .orElseThrow(() -> new DataException("ExpenditureTransaction not found", "EXPENDITURE_TRANSACTION_NOT_FOUND", 400));

            Optional<User> user= userRepo.findById(remarks.getUserId());
            ExpenditureRemarks remarkEntity = ExpenditureRemarksMapper.mapToEntity(remarks,user.get());
            remarkEntity.setBulkExpenditureTransaction(exp);

            List<ExpenditureRemarks> remarksList = new ArrayList<>();
            remarksList.add(remarkEntity);

            exp.setRemarks(remarksList);

            transactionRepo.save(exp);

        } else {
            throw new DataException("User not found for this id " + remarks.getUserId(), "USER_NOT_FOUND", 400);
        }

        return WorkflowResponse.builder()
                .message("Remark Or Response added Successfully.")
                .status(200)
                .build();
    }


}
