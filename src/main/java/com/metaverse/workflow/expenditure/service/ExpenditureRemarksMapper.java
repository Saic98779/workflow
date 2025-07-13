package com.metaverse.workflow.expenditure.service;

import com.metaverse.workflow.model.ExpenditureRemarks;
import com.metaverse.workflow.model.User;

import java.time.format.DateTimeFormatter;

public class ExpenditureRemarksMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

//    public static ExpenditureRemarksDTO mapToDto(ExpenditureRemarks entity) {
//        return new ExpenditureRemarksDTO(
//                entity.getId(),
//                entity.getUserId(),
//                entity.getRemark(),
//                entity.getRemarkDate() != null ? entity.getRemarkDate().format(DATE_FORMATTER) : null,
//                entity.getRemarkTime() != null ? entity.getRemarkTime().format(TIME_FORMATTER) : null,
//                entity.getExpenditure() != null ? entity.getExpenditure().getProgramExpenditureId() : null,
//                entity.getBulkExpenditureTransaction() != null ?entity.getBulkExpenditureTransaction().getBulkExpenditureTransactionId() : null
//        );
//    }

    public static ExpenditureRemarks mapToEntity(ExpenditureRemarksDTO dto, User user) {
        ExpenditureRemarks entity = new ExpenditureRemarks();
        entity.setId(dto.getId());
        entity.setUserId(user);
        entity.setRemark(dto.getRemark());
        return entity;
    }
}
