package com.metaverse.workflow.notifications.service;

import com.metaverse.workflow.model.Notifications;
import com.metaverse.workflow.model.NotificationMessage;
import com.metaverse.workflow.notifications.dto.NotificationDto;
import com.metaverse.workflow.notifications.dto.NotificationMessageDto;
import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.List;

@UtilityClass
public class NotificationMapper {

    // ============================================================
    // Convert Notification → NotificationDto
    // ============================================================
    public NotificationDto toDto(Notifications entity) {

        if (entity == null) return null;

        return NotificationDto.builder()
                .notificationId(entity.getId())
                .dateOfNotification(entity.getDateOfNotification())
                .dateOfFirstNotification(entity.getDateOfFirstNotification())

                // receiver
                .receiverId(entity.getReceiver() != null ? entity.getReceiver().getUserId() : null)
                .receiverName(entity.getReceiver() != null ? entity.getReceiver().getFirstName() + " " + entity.getReceiver().getLastName() : null)
                .receiverRole(entity.getReceiver() != null ? entity.getReceiver().getUserRole() : null)

                // agency
                .agencyId(entity.getAgency() != null ? entity.getAgency().getAgencyId() : null)
                .agencyName(entity.getAgency() != null ? entity.getAgency().getAgencyName() : null)

                // program
                .programId(entity.getProgram() != null ? entity.getProgram().getProgramId() : null)
                .programName(entity.getProgram() != null ? entity.getProgram().getProgramTitle() : null)

                // participant
                .participantId(entity.getParticipant() != null ? entity.getParticipant().getParticipantId() : null)
                .participantName(entity.getParticipant() != null ? entity.getParticipant().getParticipantName() : null)

                .status(entity.getStatus())
                .recipientType(entity.getRecipientType())
                .lastMessageAt(entity.getLastMessageAt())

                // messages mapped + sorted DESC
                .messages(toMessageDtoList(entity.getMessages(), entity))
                .build();
    }

    // ============================================================
    // Convert List<NotificationMessage> → List<NotificationMessageDto>
    // ============================================================
    private List<NotificationMessageDto> toMessageDtoList(List<NotificationMessage> messages, Notifications notifications) {

        if (messages == null) return List.of();

        return messages.stream()
                .sorted(Comparator.comparing(NotificationMessage::getCreatedAt).reversed())
                .map(msg -> toMessageDto(msg, notifications))
                .toList();
    }

    // ============================================================
    // Convert NotificationMessage → NotificationMessageDto
    // ============================================================
    NotificationMessageDto toMessageDto(NotificationMessage msg, Notifications notifications) {
        if (msg == null) return null;

        return NotificationMessageDto.builder()
                .id(msg.getId())
                .notificationType(notifications.getNotificationType())
                .text(msg.getText())
                .sentBy(msg.getSentBy())
                .createdAt(msg.getCreatedAt())
                .build();
    }

}
