package com.metaverse.workflow.expenditure.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.BulkExpenditure;
import com.metaverse.workflow.model.BulkExpenditureTransaction;
import com.metaverse.workflow.model.ProgramExpenditure;
import com.metaverse.workflow.model.ProgramSessionFile;

import java.util.List;

public class ExpenditureResponseMapper {


    public static BulkExpenditureResponse mapBulkExpenditure(BulkExpenditure expenditure /*, List<Long> fileIds*/)
    {
        return BulkExpenditureResponse.builder()
                .transactionId(expenditure.getTransactionId())
                .bulkExpenditureId(expenditure.getBulkExpenditureId())
                .agencyId(expenditure.getAgency().getAgencyId())
                .agencyName(expenditure.getAgency().getAgencyName())
                .itemName(expenditure.getItemName())
                .purchaseDate(DateUtil.dateToString(expenditure.getPurchaseDate(),"dd-MM-yyyy"))
                .headOfExpense(expenditure.getHeadOfExpense().getExpenseName())
                .purchasedQuantity(expenditure.getPurchasedQuantity())
                .unitCost(expenditure.getUnitCost())
                .totalCost(expenditure.getTotalCost())
                .billNo(expenditure.getBillNo())
                .billDate(DateUtil.dateToString(expenditure.getBillDate(),"dd-MM-yyyy"))
                .payeeName(expenditure.getPayeeName())
                .bankName(expenditure.getBankName())
                .ifscCode(expenditure.getIfscCode())
                .modeOfPayment(expenditure.getModeOfPayment())
                .remarks(expenditure.getRemarks())
                .uploadBillUrl(expenditure.getUploadBillUrl())
                //.fileIds(fileIds)
                .build();
    }
    public static BulkExpenditureResponse mapBulkExpenditure(BulkExpenditure expenditure , List<Long> fileIds)
    {
        return BulkExpenditureResponse.builder()
                .transactionId(expenditure.getTransactionId())
                .bulkExpenditureId(expenditure.getBulkExpenditureId())
                .agencyId(expenditure.getAgency().getAgencyId())
                .agencyName(expenditure.getAgency().getAgencyName())
                .itemName(expenditure.getItemName())
                .purchaseDate(DateUtil.dateToString(expenditure.getPurchaseDate(),"dd-MM-yyyy"))
                .headOfExpense(expenditure.getHeadOfExpense().getExpenseName())
                .purchasedQuantity(expenditure.getPurchasedQuantity())
                .unitCost(expenditure.getUnitCost())
                .totalCost(expenditure.getTotalCost())
                .billNo(expenditure.getBillNo())
                .billDate(DateUtil.dateToString(expenditure.getBillDate(),"dd-MM-yyyy"))
                .payeeName(expenditure.getPayeeName())
                .bankName(expenditure.getBankName())
                .ifscCode(expenditure.getIfscCode())
                .modeOfPayment(expenditure.getModeOfPayment())
                .remarks(expenditure.getRemarks())
                .uploadBillUrl(expenditure.getUploadBillUrl())
                .fileIds(fileIds)
                .build();
    }
    public static BulkTransactions mapBulkExpenditureTransaction(BulkExpenditureTransaction  expenditure)
    {
        return BulkTransactions.builder()

                .bulkExpenditureTransactionId(expenditure.getBulkExpenditureTransactionId())
                .itemName(expenditure.getExpenditure().getItemName())
                .purchaseDate(DateUtil.dateToString(expenditure.getExpenditure().getPurchaseDate(),"dd-MM-yyyy"))
                .purchasedQuantity(expenditure.getExpenditure().getPurchasedQuantity())
                .headOfExpense(expenditure.getHeadOfExpense().getExpenseName())
                .unitCost(expenditure.getExpenditure().getUnitCost())
                .bulkExpenditureId(expenditure.getExpenditure().getBulkExpenditureId())
                .availableQuantity(expenditure.getExpenditure().getAvailableQuantity())
                .consumedQuantity(expenditure.getConsumedQuantity())
                .allocatedCost(expenditure.getAllocatedCost())
                .expenditureType("BULK")
                .allocatedCost(expenditure.getAllocatedCost())
                .billNo(expenditure.getExpenditure().getBillNo())
                .billDate(expenditure.getExpenditure().getBillDate())
                .payeeName(expenditure.getExpenditure().getPayeeName())
                .bankName(expenditure.getExpenditure().getBankName())
                .ifscCode(expenditure.getExpenditure().getIfscCode())
                .modeOfPayment(expenditure.getExpenditure().getModeOfPayment())
                .remarks(expenditure.getExpenditure().getRemarks())
                .uploadBillUrl(expenditure.getExpenditure().getUploadBillUrl())
                .build();
    }

    public static ProgramExpenditureResponse mapProgramExpenditure(ProgramExpenditure expenditure)
    {
        return ProgramExpenditureResponse.builder()
                .transactionId(expenditure.getTransactionId())
                .programExpenditureId(expenditure.getProgramExpenditureId())
                .activityId(expenditure.getActivity().getActivityId())
                .activityName(expenditure.getActivity().getActivityName())
                .subActivityId(expenditure.getSubActivity().getSubActivityId())
                .subActivityName(expenditure.getSubActivity().getSubActivityName())
                .programId(expenditure.getProgram().getProgramId())
                .programName(expenditure.getProgram().getProgramTitle())
                .agencyId(expenditure.getAgency().getAgencyId())
                .agencyName(expenditure.getAgency().getAgencyName())
                .expenditureType(expenditure.getExpenditureType().toString())
                .headOfExpense(expenditure.getHeadOfExpense().getExpenseName())
                .cost(expenditure.getCost())
                .billNo(expenditure.getBillNo())
                .billDate(DateUtil.dateToString(expenditure.getBillDate(),"dd-MM-yyyy"))
                .payeeName(expenditure.getPayeeName())
                .bankName(expenditure.getBankName())
                .ifscCode(expenditure.getIfscCode())
                .modeOfPayment(expenditure.getModeOfPayment())
                .purpose(expenditure.getPurpose())
                .uploadBillUrl(expenditure.getUploadBillUrl())

                .build();

    }
    public static ProgramExpenditureResponse mapProgramExpenditure(ProgramExpenditure expenditure,List<Long> fileIds)
    {
        return ProgramExpenditureResponse.builder()
                .transactionId(expenditure.getTransactionId())
                .programExpenditureId(expenditure.getProgramExpenditureId())
                .activityId(expenditure.getActivity().getActivityId())
                .activityName(expenditure.getActivity().getActivityName())
                .subActivityId(expenditure.getSubActivity().getSubActivityId())
                .subActivityName(expenditure.getSubActivity().getSubActivityName())
                .programId(expenditure.getProgram().getProgramId())
                .programName(expenditure.getProgram().getProgramTitle())
                .agencyId(expenditure.getAgency().getAgencyId())
                .agencyName(expenditure.getAgency().getAgencyName())
                .expenditureType(expenditure.getExpenditureType().toString())
                .headOfExpense(expenditure.getHeadOfExpense().getExpenseName())
                .cost(expenditure.getCost())
                .billNo(expenditure.getBillNo())
                .billDate(DateUtil.dateToString(expenditure.getBillDate(),"dd-MM-yyyy"))
                .payeeName(expenditure.getPayeeName())
                .bankName(expenditure.getBankName())
                .ifscCode(expenditure.getIfscCode())
                .modeOfPayment(expenditure.getModeOfPayment())
                .purpose(expenditure.getPurpose())
                .uploadBillUrl(expenditure.getUploadBillUrl())
                .fileIds(fileIds)
                .build();

    }

    public static BulkExpenditureLookupResponse mapBulkExpenditureDetails(BulkExpenditure  expenditure)
    {
        return BulkExpenditureLookupResponse.builder()
                //.bulkExpenditureTransactionId()
                .purchasedQuantity(expenditure.getPurchasedQuantity())
                .bulkExpenditureId(expenditure.getBulkExpenditureId())
                .unitCost(expenditure.getUnitCost())
                .purchaseDate(DateUtil.dateToString(expenditure.getPurchaseDate(),"dd-MM-yyyy"))
                .consumedQuantity(expenditure.getConsumedQuantity())
                .availableQuantity(expenditure.getAvailableQuantity())
                .build();

    }

}
