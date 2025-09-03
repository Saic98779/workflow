package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.*;

public class NonTrainingExpenditureMapper {
    public static NonTrainingExpenditureDTO toDTO(NonTrainingExpenditure entity) {
        NonTrainingExpenditureDTO dto = new NonTrainingExpenditureDTO();
        dto.setId(entity.getId());
        dto.setPaymentDate(DateUtil.dateToString(entity.getPaymentDate(),"dd-MM-yyy"));
        dto.setNonTrainingSubActivityId(entity.getNonTrainingSubActivity().getSubActivityId());
        dto.setNonTrainingActivityId(entity.getNonTrainingSubActivity().getSubActivityId());
        dto.setAgencyId(entity.getAgency().getAgencyId());
        dto.setCategory(entity.getCategory());
        dto.setDateOfPurchase(entity.getDateOfPurchase());
        dto.setExpenditureAmount(entity.getExpenditureAmount());
        dto.setBillNo(entity.getBillNo());
        dto.setBillDate(DateUtil.dateToString(entity.getBillDate(),"dd-MM-yyy"));
        dto.setPayeeName(entity.getPayeeName());
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setBankName(entity.getBankName());
        dto.setIfscCode(entity.getIfscCode());
        dto.setModeOfPayment(entity.getModeOfPayment());
        dto.setTransactionId(entity.getTransactionId());
        dto.setPurpose(entity.getPurpose());
        dto.setUploadBillUrl(entity.getUploadBillUrl());
        return dto;
    }

    public static NonTrainingExpenditure toEntity(NonTrainingExpenditureDTO dto, Agency agency, NonTrainingActivity activity, NonTrainingSubActivity subActivity) {
        NonTrainingExpenditure entity = new NonTrainingExpenditure();
        entity.setId(dto.getId());
        entity.setAgency(agency);
        entity.setNonTrainingSubActivity(subActivity);
        entity.setCategory(dto.getCategory());
        entity.setDateOfPurchase(dto.getDateOfPurchase());
        entity.setPaymentDate(DateUtil.stringToDate(dto.getPaymentDate(),"dd-MM-yyyy"));
        entity.setExpenditureAmount(dto.getExpenditureAmount());
        entity.setBillNo(dto.getBillNo());
        entity.setBillDate(DateUtil.stringToDate(dto.getBillDate(),"dd-MM-yyyy"));
        entity.setPayeeName(dto.getPayeeName());
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setBankName(dto.getBankName());
        entity.setIfscCode(dto.getIfscCode());
        entity.setModeOfPayment(dto.getModeOfPayment());
        entity.setTransactionId(dto.getTransactionId());
        entity.setPurpose(dto.getPurpose());
        entity.setUploadBillUrl(dto.getUploadBillUrl());
        entity.setNonTrainingActivity(activity);
        return entity;
    }


    public static NonTrainingResource mapToResource(NonTrainingResourceDTO dto,NonTrainingSubActivity nonTrainingsubActivity) {
        if (dto == null) return null;

        NonTrainingResource entity = new NonTrainingResource();
        entity.setName(dto.getName());
        entity.setDesignation(dto.getDesignation());
        entity.setRelevantExperience(dto.getRelevantExperience());
        entity.setEducationalQualifications(dto.getEducationalQualification());
        entity.setDateOfJoining(DateUtil.stringToDate(dto.getDateOfJoining(),"dd-MM-yyyy"));
        entity.setMonthlySal(dto.getMonthlySal());
        entity.setBankName(dto.getBankName());
        entity.setIfscCode(dto.getIfscCode());
        entity.setAccountNo(dto.getAccountNo());
//        entity.setNonTrainingActivity(nonTrainingActivity);
        return entity;
    }

    public static NonTrainingResourceDTO mapToResourceRes(NonTrainingResource dto) {
        if (dto == null) return null;

        NonTrainingResourceDTO entity = new NonTrainingResourceDTO();
        entity.setResourceId(dto.getResourceId());
        entity.setName(dto.getName());
        entity.setDesignation(dto.getDesignation());
        entity.setRelevantExperience(dto.getRelevantExperience());
        entity.setEducationalQualification(dto.getEducationalQualifications());
        entity.setDateOfJoining(DateUtil.dateToString(dto.getDateOfJoining(),"dd-MM-yyyy"));
        entity.setMonthlySal(dto.getMonthlySal());
        entity.setBankName(dto.getBankName());
        entity.setIfscCode(dto.getIfscCode());
        entity.setAccountNo(dto.getAccountNo());
        entity.setExpenditures(dto.getNonTrainingResourceExpenditures().stream().map(NonTrainingExpenditureMapper::mapToResourceExpenditureResponse).toList());
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
        dto.setResourceId(entity.getNonTrainingResource().getResourceId());
        dto.setResourceName(entity.getNonTrainingResource().getName());
        dto.setUploadBillUrl(entity.getUploadBillUrl());
        return dto;
    }
    public static  NonTrainingResourceDTO mapToResourceResForDropdown(NonTrainingResource  nonTrainingResource)
    {
        if(nonTrainingResource == null) return null;
        NonTrainingResourceDTO resource = new NonTrainingResourceDTO();
        resource.setResourceId(nonTrainingResource.getResourceId());
        resource.setName(nonTrainingResource.getName());
        resource.setBankName(nonTrainingResource.getBankName());
        resource.setAccountNo(nonTrainingResource.getAccountNo());
        resource.setIfscCode(nonTrainingResource.getIfscCode());
        return resource;

    }

}
