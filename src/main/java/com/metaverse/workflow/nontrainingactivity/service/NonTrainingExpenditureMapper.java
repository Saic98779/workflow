package com.metaverse.workflow.nontrainingactivity.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.*;

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


    public static NonTrainingResource mapToResource(NonTrainingResourceDTO dto) {
        if (dto == null) return null;

        NonTrainingResource entity = new NonTrainingResource();
        entity.setName(dto.getName());
        entity.setDesignation(dto.getDesignation());
        entity.setRelevantExperience(dto.getRelevantExperience());
        entity.setEducationalQualifications(dto.getEducationalQualifications());
        entity.setDateOfJoining(DateUtil.stringToDate(dto.getDateOfJoining(),"dd-MM-yyyy"));
        entity.setMonthlySal(dto.getMonthlySal());
        entity.setBankName(dto.getBankName());
        entity.setIfscCode(dto.getIfscCode());
        entity.setAccountNo(dto.getAccountNo());
        return entity;
    }

    public static NonTrainingResourceDTO mapToResourceRes(NonTrainingResource dto) {
        if (dto == null) return null;

        NonTrainingResourceDTO entity = new NonTrainingResourceDTO();
        entity.setResourceId(dto.getResourceId());
        entity.setName(dto.getName());
        entity.setDesignation(dto.getDesignation());
        entity.setRelevantExperience(dto.getRelevantExperience());
        entity.setEducationalQualifications(dto.getEducationalQualifications());
        entity.setDateOfJoining(DateUtil.dateToString(dto.getDateOfJoining(),"dd-MM-yyyy"));
        entity.setMonthlySal(dto.getMonthlySal());
        entity.setBankName(dto.getBankName());
        entity.setIfscCode(dto.getIfscCode());
        entity.setAccountNo(dto.getAccountNo());
        return entity;
    }

    public static NonTrainingResourceExpenditure mapToResourceExpenditure(NonTrainingResourceExpenditureDTO dto,NonTrainingResource resource) {
        if (dto == null) return null;
        NonTrainingResourceExpenditure entity = new NonTrainingResourceExpenditure();
        entity.setAmount(dto.getAmount());
        entity.setPaymentForMonth(dto.getPaymentForMonth());
        entity.setDateOfPayment(DateUtil.stringToDate(dto.getDateOfPayment(),"dd-MM-yyyy"));
        entity.setNonTrainingResource(resource);
        return entity;
    }




    public static NonTrainingResourceExpenditureDTO mapToResourceExpenditureResponse(NonTrainingResourceExpenditure entity) {
        if (entity == null) return null;

        NonTrainingResourceExpenditureDTO dto = new NonTrainingResourceExpenditureDTO();
        dto.setNonTrainingResourceExpenditureId(entity.getNonTrainingResourceExpenditureId());
        dto.setAmount(entity.getAmount());
        dto.setPaymentForMonth(entity.getPaymentForMonth());
        dto.setDateOfPayment(DateUtil.dateToString(entity.getDateOfPayment(),"dd-MM-yyyy"));
        return dto;
    }


}
