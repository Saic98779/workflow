package com.metaverse.workflow.nontrainingactivity.service;

import com.metaverse.workflow.model.Activity;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.NonTrainingExpenditure;

public class NonTrainingExpenditureMapper {
    public static NonTrainingExpenditureDTO toDTO(NonTrainingExpenditure entity) {
        NonTrainingExpenditureDTO dto = new NonTrainingExpenditureDTO();
        dto.setId(entity.getId());
        dto.setPaymentDate(entity.getPaymentDate());
        dto.setExpenditureAmount(entity.getExpenditureAmount());
        dto.setBillNo(entity.getBillNo());
        dto.setBillDate(entity.getBillDate());
        dto.setPayeeName(entity.getPayeeName());
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setBankName(entity.getBankName());
        dto.setIfscCode(entity.getIfscCode());
        dto.setModeOfPayment(entity.getModeOfPayment());
        dto.setTransactionId(entity.getTransactionId());
        dto.setPurpose(entity.getPurpose());
        dto.setUploadBillUrl(entity.getUploadBillUrl());
        dto.setCreatedOn(entity.getCreatedOn());
        dto.setUpdatedOn(entity.getUpdatedOn());
        return dto;
    }

    public static NonTrainingExpenditure toEntity(NonTrainingExpenditureDTO dto, Agency agency, Activity activity) {
        NonTrainingExpenditure entity = new NonTrainingExpenditure();
        entity.setId(dto.getId());
        entity.setAgency(agency);
        entity.setActivityId(activity);
        entity.setPaymentDate(dto.getPaymentDate());
        entity.setExpenditureAmount(dto.getExpenditureAmount());
        entity.setBillNo(dto.getBillNo());
        entity.setBillDate(dto.getBillDate());
        entity.setPayeeName(dto.getPayeeName());
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setBankName(dto.getBankName());
        entity.setIfscCode(dto.getIfscCode());
        entity.setModeOfPayment(dto.getModeOfPayment());
        entity.setTransactionId(dto.getTransactionId());
        entity.setPurpose(dto.getPurpose());
        entity.setUploadBillUrl(dto.getUploadBillUrl());
        entity.setCreatedOn(dto.getCreatedOn());
        entity.setUpdatedOn(dto.getUpdatedOn());
        return entity;
    }
}
