package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.*;

public class NonTrainingExpenditureMapper {
    public static NonTrainingExpenditureDTO toDTO(NonTrainingExpenditure entity) {
        NonTrainingExpenditureDTO dto = new NonTrainingExpenditureDTO();
        dto.setId(entity.getId());
        dto.setPaymentDate(DateUtil.dateToString(entity.getPaymentDate(), "dd-MM-yyy"));
        dto.setNonTrainingSubActivityId(entity.getNonTrainingSubActivity().getSubActivityId());
        dto.setNonTrainingActivityId(entity.getNonTrainingSubActivity().getSubActivityId());
        dto.setAgencyId(entity.getAgency().getAgencyId());
        dto.setCategory(entity.getCategory());
        dto.setDateOfPurchase(entity.getDateOfPurchase());
        dto.setExpenditureAmount(entity.getExpenditureAmount());
        dto.setBillNo(entity.getBillNo());
        dto.setBillDate(DateUtil.dateToString(entity.getBillDate(), "dd-MM-yyy"));
        dto.setPayeeName(entity.getPayeeName());
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setBankName(entity.getBankName());
        dto.setIfscCode(entity.getIfscCode());
        dto.setModeOfPayment(entity.getModeOfPayment());
        dto.setTransactionId(entity.getTransactionId());
        dto.setPurpose(entity.getPurpose());
        dto.setUploadBillUrl(entity.getUploadBillUrl());
        dto.setCheckNo(entity.getCheckNo());
        dto.setCheckDate(DateUtil.dateToString(entity.getCheckDate(),"dd-MM-YYYY"));
        return dto;
    }

    public static NonTrainingExpenditure toEntity(NonTrainingExpenditureDTO dto, Agency agency, NonTrainingActivity activity, NonTrainingSubActivity subActivity) {
        NonTrainingExpenditure entity = new NonTrainingExpenditure();
        entity.setId(dto.getId());
        entity.setAgency(agency);
        entity.setNonTrainingSubActivity(subActivity);
        entity.setCategory(dto.getCategory());
        entity.setDateOfPurchase(dto.getDateOfPurchase());
        entity.setPaymentDate(DateUtil.covertStringToDate(dto.getPaymentDate()));
        entity.setExpenditureAmount(dto.getExpenditureAmount());
        entity.setBillNo(dto.getBillNo());
        entity.setBillDate(DateUtil.covertStringToDate(dto.getBillDate()));
        entity.setPayeeName(dto.getPayeeName());
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setBankName(dto.getBankName());
        entity.setIfscCode(dto.getIfscCode());
        entity.setModeOfPayment(dto.getModeOfPayment());
        entity.setTransactionId(dto.getTransactionId());
        entity.setPurpose(dto.getPurpose());
        entity.setUploadBillUrl(dto.getUploadBillUrl());
        entity.setNonTrainingActivity(activity);
        entity.setCheckNo(dto.getCheckNo());
        entity.setCheckDate(DateUtil.covertStringToDate(dto.getCheckDate()));
        return entity;
    }


    public static NonTrainingResource mapToResource(NonTrainingResourceDTO dto, NonTrainingSubActivity nonTrainingsubActivity,NonTrainingActivity nonTrainingActivity) {
        if (dto == null) return null;

        NonTrainingResource entity = new NonTrainingResource();
        entity.setName(dto.getName());
        entity.setDesignation(dto.getDesignation());
        entity.setRelevantExperience(dto.getRelevantExperience());
        entity.setEducationalQualifications(dto.getEducationalQualification());
        entity.setDateOfJoining(DateUtil.covertStringToDate(dto.getDateOfJoining()));
        entity.setMonthlySal(dto.getMonthlySal());
        entity.setBankName(dto.getBankName());
        entity.setIfscCode(dto.getIfscCode());
        entity.setAccountNo(dto.getAccountNo());
        entity.setNonTrainingSubActivity(nonTrainingsubActivity);
        entity.setNonTrainingActivity(nonTrainingActivity);
        entity.setSector(dto.getSector());
        entity.setMethodOfSelection(dto.getMethodOfSelection());
        entity.setNameOfTheCompany(dto.getNameOfTheCompany());
        return entity;
    }

    public static NonTrainingResourceDTO mapToResourceRes(NonTrainingResource resource) {
        if (resource == null) return null;

        NonTrainingResourceDTO entity = new NonTrainingResourceDTO();
        entity.setResourceId(resource.getResourceId());
        entity.setName(resource.getName());
        entity.setDesignation(resource.getDesignation());
        entity.setRelevantExperience(resource.getRelevantExperience());
        entity.setEducationalQualification(resource.getEducationalQualifications());
        entity.setDateOfJoining(DateUtil.dateToString(resource.getDateOfJoining(), "dd-MM-yyyy"));
        entity.setMonthlySal(resource.getMonthlySal());
        entity.setBankName(resource.getBankName());
        entity.setIfscCode(resource.getIfscCode());
        entity.setAccountNo(resource.getAccountNo());
        entity.setExpenditures(resource.getNonTrainingResourceExpenditures().stream().map(NonTrainingExpenditureMapper::mapToResourceExpenditureResponse).toList());
        entity.setSector(resource.getSector());
        entity.setMethodOfSelection(resource.getMethodOfSelection());
        entity.setNameOfTheCompany(resource.getNameOfTheCompany());
        return entity;
    }

    public static NonTrainingResourceExpenditure mapToResourceExpenditure(NonTrainingResourceExpenditureDTO dto, NonTrainingResource resource) {
        if (dto == null) return null;
        NonTrainingResourceExpenditure entity = new NonTrainingResourceExpenditure();
        entity.setAmount(dto.getAmount());
        entity.setPaymentForMonth(dto.getPaymentForMonth());
        entity.setDateOfPayment(DateUtil.covertStringToDate(dto.getDateOfPayment()));
        entity.setNonTrainingResource(resource);
        return entity;
    }


    public static NonTrainingResourceExpenditureDTO mapToResourceExpenditureResponse(NonTrainingResourceExpenditure entity) {
        if (entity == null) return null;

        NonTrainingResourceExpenditureDTO dto = new NonTrainingResourceExpenditureDTO();
        dto.setNonTrainingResourceExpenditureId(entity.getNonTrainingResourceExpenditureId());
        dto.setAmount(entity.getAmount());
        dto.setPaymentForMonth(entity.getPaymentForMonth());
        dto.setDateOfPayment(DateUtil.dateToString(entity.getDateOfPayment(), "dd-MM-yyyy"));
        dto.setResourceId(entity.getNonTrainingResource().getResourceId());
        dto.setResourceName(entity.getNonTrainingResource().getName());
        dto.setUploadBillUrl(entity.getUploadBillUrl());
        return dto;
    }

    public static NonTrainingResourceDTO mapToResourceResForDropdown(NonTrainingResource nonTrainingResource) {
        if (nonTrainingResource == null) return null;
        NonTrainingResourceDTO resource = new NonTrainingResourceDTO();
        resource.setResourceId(nonTrainingResource.getResourceId());
        resource.setName(nonTrainingResource.getName());
        resource.setBankName(nonTrainingResource.getBankName());
        resource.setAccountNo(nonTrainingResource.getAccountNo());
        resource.setIfscCode(nonTrainingResource.getIfscCode());
        return resource;

    }

    public static NonTrainingSpiuComments mapToEntitySpiuComments(NonTrainingExpenditureRemarksDTO dto, User user) {
        NonTrainingSpiuComments entity = new NonTrainingSpiuComments();
        entity.setUserId(user);
        entity.setRemarks(dto.getSpiuComments());
        return entity;
    }

    public static NonTrainingAgencyComments mapToEntityAgencyComments(NonTrainingExpenditureRemarksDTO dto, User user) {
        NonTrainingAgencyComments entity = new NonTrainingAgencyComments();
        entity.setUserId(user);
        entity.setRemarks(dto.getAgencyComments());
        return entity;
    }

}
