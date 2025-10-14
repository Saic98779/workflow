package com.metaverse.workflow.notifications.dto;
import com.metaverse.workflow.enums.NotificationRecipientType;
import com.metaverse.workflow.enums.RemarkBy;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalNotificationDTO {
    private Long id;
    private String message;
    private RemarkBy remarkBy;
    private NotificationRecipientType recipientType;
    private Boolean isRead;
    private String dateOfNotification;
    private String createdById;
    private String recipientUserId;
    private Long agencyId;
}
