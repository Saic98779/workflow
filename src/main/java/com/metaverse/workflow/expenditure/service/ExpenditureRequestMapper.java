package com.metaverse.workflow.expenditure.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.*;

public class ExpenditureRequestMapper {

    public static BulkExpenditure mapBulkExpenditure(BulkExpenditureRequest request, Agency agency, HeadOfExpense headOfExpense) {
        return BulkExpenditure.builder()
                .transactionId(request.getTransactionId())
                .agency(agency)
                .headOfExpense(headOfExpense)
                .itemName(request.getItemName())
                .purchaseDate(DateUtil.stringToDate(request.getPurchaseDate(), "dd-MM-yyyy"))
                .purchasedQuantity(request.getPurchasedQuantity())
                .availableQuantity(request.getPurchasedQuantity())
                .consumedQuantity(0)
                .unitCost(request.getUnitCost())
                .totalCost(request.getUnitCost() * request.getPurchasedQuantity())
                .billNo(request.getBillNo())
                .billDate(DateUtil.stringToDate(request.getBillDate(), "dd-MM-yyyy"))
                .payeeName(request.getPayeeName())
                .bankName(request.getBankName())
                .ifscCode(request.getIfscCode())
                .modeOfPayment(request.getModeOfPayment().toString())
                .remarks(request.getRemarks())
                .uploadBillUrl(request.getUploadBillUrl())
                .build();

    }

    public static ProgramExpenditure mapProgramExpenditure(ProgramExpenditureRequest request, Activity activity, SubActivity subActivity, Program program, Agency agency, HeadOfExpense headOfExpense) {
        return ProgramExpenditure.builder()
                .transactionId(request.getTransactionId())
                .activity(activity)
                .subActivity(subActivity)
                .program(program)
                .agency(agency)
                .expenditureType(request.getExpenditureType())
                .headOfExpense(headOfExpense)
                .cost(request.getCost())
                .billNo(request.getBillNo())
                .billDate(DateUtil.stringToDate(request.getBillDate(), "dd-MM-yyyy"))
                .payeeName(request.getPayeeName())
                .bankName(request.getBankName())
                .ifscCode(request.getIfscCode())
                .modeOfPayment(request.getModeOfPayment().toString())
                .purpose(request.getPurpose())
                .uploadBillUrl(request.getUploadBillUrl())
                .build();

    }

    public static void updateProgramExpenditure(ProgramExpenditure existing, ProgramExpenditureRequest request, Activity activity, SubActivity subActivity,
                                                Program program, Agency agency, HeadOfExpense headOfExpense) {
        existing.setTransactionId(request.getTransactionId());
        existing.setActivity(activity);
        existing.setSubActivity(subActivity);
        existing.setProgram(program);
        existing.setAgency(agency);
        existing.setExpenditureType(request.getExpenditureType());
        existing.setHeadOfExpense(headOfExpense);
        existing.setCost(request.getCost());
        existing.setBillNo(request.getBillNo());
        existing.setBillDate(DateUtil.stringToDate(request.getBillDate(), "dd-MM-yyyy"));
        existing.setPayeeName(request.getPayeeName());
        existing.setBankName(request.getBankName());
        existing.setIfscCode(request.getIfscCode());
        existing.setModeOfPayment(request.getModeOfPayment().toString());
        existing.setPurpose(request.getPurpose());
        existing.setUploadBillUrl(request.getUploadBillUrl());


    }


    public static BulkExpenditureTransaction mapBulkExpenditureTransaction(BulkExpenditureTransactionRequest request, Activity activity, SubActivity subActivity, Program program, Agency agency, BulkExpenditure bulkExpenditure, HeadOfExpense headOfExpense) {
        return BulkExpenditureTransaction.builder()
                .activity(activity)
                .expenditure(bulkExpenditure)
                .subActivity(subActivity)
                .program(program)
                .agency(agency)
                .headOfExpense(headOfExpense)
                .consumedQuantity(request.getConsumedQuantity())
                .allocatedCost(request.getAllocatedCost())
                .build();
    }


    public static void updateBulkExpenditure(BulkExpenditure existingExpenditure, BulkExpenditureRequest expenditureRequest, Agency agency, HeadOfExpense headOfExpense) {
        existingExpenditure.setAgency(agency);
        existingExpenditure.setHeadOfExpense(headOfExpense);
        existingExpenditure.setItemName(expenditureRequest.getItemName());
        existingExpenditure.setPurchaseDate(DateUtil.stringToDate(expenditureRequest.getPurchaseDate(), "dd-MM-yyyy"));
        existingExpenditure.setPurchasedQuantity(expenditureRequest.getPurchasedQuantity());
        existingExpenditure.setAvailableQuantity(expenditureRequest.getPurchasedQuantity()); // Optional: only if full reset is desired
        existingExpenditure.setConsumedQuantity(existingExpenditure.getConsumedQuantity()); // Optional: reset logic – change if not desired during update
        existingExpenditure.setUnitCost(expenditureRequest.getUnitCost());
        existingExpenditure.setTotalCost(expenditureRequest.getUnitCost() * expenditureRequest.getPurchasedQuantity());
        existingExpenditure.setBillNo(expenditureRequest.getBillNo());
        existingExpenditure.setBillDate(DateUtil.stringToDate(expenditureRequest.getBillDate(), "dd-MM-yyyy"));
        existingExpenditure.setPayeeName(expenditureRequest.getPayeeName());
        existingExpenditure.setBankName(expenditureRequest.getBankName());
        existingExpenditure.setIfscCode(expenditureRequest.getIfscCode());
        existingExpenditure.setModeOfPayment(expenditureRequest.getModeOfPayment().toString());
        existingExpenditure.setRemarks(expenditureRequest.getRemarks());
        existingExpenditure.setUploadBillUrl(expenditureRequest.getUploadBillUrl());
    }
}
