package com.metaverse.workflow.expenditure.service;

import com.metaverse.workflow.model.AgencyComments;
import com.metaverse.workflow.model.SpiuComments;
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

    public static SpiuComments mapToEntity(ExpenditureRemarksDTO dto, User user) {
        SpiuComments entity = new SpiuComments();
        entity.setUserId(user);
        entity.setRemarks(dto.getSpiuComments());
        return entity;
    }

    public static AgencyComments mapToEntityAgencyComments(ExpenditureRemarksDTO dto, User user) {
        AgencyComments entity = new AgencyComments();
        entity.setUserId(user);
        entity.setRemarks(dto.getAgencyComments());
        return entity;
    }
}
