package com.metaverse.workflow.participant.service;

import com.metaverse.workflow.model.ParticipantTemp;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParticipantTempResponseMapper {

    default ParticipantTempResponse convertToEntity(ParticipantTemp dto) {
        ParticipantTempResponse entity = new ParticipantTempResponse();
        entity.setParticipantName(dto.getParticipantName());
        entity.setGender(dto.getGender());
        entity.setCategory(dto.getCategory());
        entity.setDisability(dto.getDisability());
        entity.setAadharNo(dto.getAadharNo());
        entity.setMobileNo(dto.getMobileNo());
        entity.setEmail(dto.getEmail());
        entity.setDesignation(dto.getDesignation());
        entity.setIsParticipatedBefore(dto.getIsParticipatedBefore());
        entity.setPreviousParticipationDetails(dto.getPreviousParticipationDetails());
        entity.setPreTrainingAssessmentConducted(dto.getPreTrainingAssessmentConducted());
        entity.setPostTrainingAssessmentConducted(dto.getPostTrainingAssessmentConducted());
        entity.setIsCertificateIssued(dto.getIsCertificateIssued());
        entity.setCertificateIssueDate(dto.getCertificateIssueDate());
        entity.setNeedAssessmentMethodology(dto.getNeedAssessmentMethodology());
        entity.setHasError(dto.getHasError());
        entity.setErrorMessage(dto.getErrorMessage());
        entity.setIsDeleted(dto.getIsDeleted());
        return entity;
    }
}
