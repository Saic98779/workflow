package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.NonTrainingConsumablesBulk;

public class NonTrainingConsumablesBulkMapper {

    public static NonTrainingConsumablesBulk mapToBulkConsumable(NonTrainingConsumablesBulkDto dto) {
        NonTrainingConsumablesBulk e = new NonTrainingConsumablesBulk();
        e.setItemName(dto.getItemName());
        e.setPurchaseDate(DateUtil.covertStringToDate(dto.getPurchaseDate()));
        e.setPurchasedQuantity(dto.getPurchasedQuantity());
        e.setUnitCost(dto.getUnitCost());
        e.setConsumedQuantity(dto.getConsumedQuantity());
        e.setAvailableQuantity(dto.getPurchasedQuantity());
        e.setTotalCost(dto.getTotalCost());
        e.setBillNo(dto.getBillNo());
        e.setBillDate(DateUtil.covertStringToDate(dto.getBillDate()));
        e.setModeOfPayment(dto.getModeOfPayment());
        e.setPayeeName(dto.getPayeeName());
        e.setBankName(dto.getBankName());
        e.setIfscCode(dto.getIfscCode());
        e.setTransactionId(dto.getTransactionId());
        e.setCheckNo(dto.getCheckNo());
        e.setCheckDate(DateUtil.covertStringToDate(dto.getCheckDate()));
        e.setPurchaseOrderNo(dto.getPurchaseOrderNo());
        e.setNameOfTheVendor(dto.getNameOfTheVendor());
        return e;
    }

    public static NonTrainingConsumablesBulkDto mapToBulkConsumableDto(NonTrainingConsumablesBulk e) {
        NonTrainingConsumablesBulkDto dto = new NonTrainingConsumablesBulkDto();
        dto.setId(e.getId());
        dto.setItemName(e.getItemName());
        dto.setPurchaseDate(DateUtil.dateToString(e.getPurchaseDate(),"dd-MM-yyyy"));
        dto.setPurchasedQuantity(e.getPurchasedQuantity());
        dto.setUnitCost(e.getUnitCost());
        dto.setConsumedQuantity(e.getConsumedQuantity());
        dto.setAvailableQuantity(e.getAvailableQuantity());
        dto.setTotalCost(e.getTotalCost());
        dto.setBillNo(e.getBillNo());
        dto.setBillDate(DateUtil.dateToString(e.getBillDate(),"dd-MM-yyyy"));
        dto.setModeOfPayment(e.getModeOfPayment());
        dto.setPayeeName(e.getPayeeName());
        dto.setBankName(e.getBankName());
        dto.setIfscCode(e.getIfscCode());
        dto.setTransactionId(e.getTransactionId());
        dto.setCheckNo(e.getCheckNo());
        dto.setCheckDate(DateUtil.dateToString(e.getBillDate(),"dd-MM-yyyy"));
        dto.setUploadBillUrl(e.getUploadBillUrl());
        dto.setAgencyId(e.getAgency().getAgencyId());
        dto.setSubActivityId(e.getNonTrainingSubActivity().getSubActivityId());
        dto.setPurchaseOrderNo(e.getPurchaseOrderNo());
        dto.setNameOfTheVendor(e.getNameOfTheVendor());
        return dto;
    }

    public static NonTrainingConsumablesBulk updateBulkConsumable(NonTrainingConsumablesBulk existing, NonTrainingConsumablesBulkDto dto) {
        existing.setItemName(dto.getItemName() != null ? dto.getItemName() : existing.getItemName());
        existing.setPurchaseDate(dto.getPurchaseDate() != null ? DateUtil.covertStringToDate(dto.getPurchaseDate()) : existing.getPurchaseDate());
        existing.setPurchasedQuantity(dto.getPurchasedQuantity() != null ? dto.getPurchasedQuantity() : existing.getPurchasedQuantity());
        existing.setUnitCost(dto.getUnitCost() != null ? dto.getUnitCost() : existing.getUnitCost());
        existing.setConsumedQuantity(dto.getConsumedQuantity() != null ? dto.getConsumedQuantity() : existing.getConsumedQuantity());
        existing.setAvailableQuantity(dto.getAvailableQuantity() != null ? dto.getAvailableQuantity() : existing.getAvailableQuantity());
        existing.setTotalCost(dto.getTotalCost() != null ? dto.getTotalCost() : existing.getTotalCost());
        existing.setBillNo(dto.getBillNo() != null ? dto.getBillNo() : existing.getBillNo());
        existing.setBillDate(dto.getBillDate() != null ? DateUtil.covertStringToDate(dto.getBillDate()) : existing.getBillDate());
        existing.setModeOfPayment(dto.getModeOfPayment() != null ? dto.getModeOfPayment() : existing.getModeOfPayment());
        existing.setPayeeName(dto.getPayeeName() != null ? dto.getPayeeName() : existing.getPayeeName());
        existing.setBankName(dto.getBankName() != null ? dto.getBankName() : existing.getBankName());
        existing.setIfscCode(dto.getIfscCode() != null ? dto.getIfscCode() : existing.getIfscCode());
        existing.setTransactionId(dto.getTransactionId() != null ? dto.getTransactionId() : existing.getTransactionId());
        existing.setCheckNo(dto.getCheckNo() != null ? dto.getCheckNo() : existing.getCheckNo());
        existing.setCheckDate(dto.getCheckDate() != null ? DateUtil.covertStringToDate(dto.getCheckDate()) : existing.getCheckDate());
        existing.setNameOfTheVendor(dto.getNameOfTheVendor() != null ?dto.getNameOfTheVendor() : existing.getNameOfTheVendor());
        existing.setPurchaseOrderNo(dto.getPurchaseOrderNo() != null ?dto.getPurchaseOrderNo() : existing.getPurchaseOrderNo());

        return existing;
    }

}

