package com.metaverse.workflow.notifications.dto;

import com.metaverse.workflow.model.NotificationRemark;
import com.metaverse.workflow.model.Notifications;

public class NotificationMapper {

    public static NotificationResponseDto toDto(Notifications entity) {
        NotificationResponseDto dto = new NotificationResponseDto();

        dto.setId(entity.getId());
        dto.setDateOfNotification(entity.getDateOfNotification());
        dto.setDateOfFirstNotification(entity.getDateOfFirstNotification());

        dto.setCallCenterAgentName(
                entity.getCallCenterAgent() != null
                        ? entity.getCallCenterAgent().getFirstName() + " " + entity.getCallCenterAgent().getLastName()
                        : null
        );
        dto.setAgencyName(entity.getAgency().getAgencyName());
        dto.setParticipantName(entity.getParticipant().getParticipantName() != null ? entity.getParticipant().getParticipantName() : null);
        dto.setProgramName(entity.getProgram() != null ? entity.getProgram().getProgramTitle() : null);

        dto.setStatus(entity.getStatus());
        dto.setDateOfFix(entity.getDateOfFix());
        dto.setDateOfClosure(entity.getDateOfClosure());
        dto.setRecipientType(entity.getRecipientType());

        dto.setRemarksByAgency(
                entity.getRemarksByAgency().stream()
                        .map( agencyRemarks -> new RemarksResponseDto(agencyRemarks.getRemarkText(),agencyRemarks.getRemarkedAt()+"")).toList()
        );
        dto.setRemarksByCallCenter(
                entity.getRemarksByCallCenter().stream()
                        .map( callCenterRemarks -> new RemarksResponseDto(callCenterRemarks.getRemarkText(),callCenterRemarks.getRemarkedAt()+"")).toList()
        );

        return dto;
    }
}
