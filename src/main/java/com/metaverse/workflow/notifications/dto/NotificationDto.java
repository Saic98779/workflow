package com.metaverse.workflow.notifications.dto;

import com.metaverse.workflow.enums.NotificationRecipientType;
import com.metaverse.workflow.enums.NotificationStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {

    private Long notificationId;

    private LocalDateTime dateOfNotification;
    private LocalDateTime dateOfFirstNotification;

    // Receiver details
    private String receiverId;
    private String receiverName;
    private String receiverRole;

    // Linked entities
    private Long agencyId;
    private String agencyName;

    private Long programId;
    private String programName;

    private Long participantId;
    private String participantName;

    // Status
    private NotificationStatus status;
    private NotificationRecipientType recipientType;

    // Last message time
    private LocalDateTime lastMessageAt;
    private Boolean isRead;

    // All messages in this thread
    private List<NotificationMessageDto> messages;
}
