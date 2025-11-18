package com.metaverse.workflow.notifications.dto;

import com.metaverse.workflow.common.enums.NotificationType;
import com.metaverse.workflow.enums.RemarkBy;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GlobalNotificationResponse {

    private Long notificationId;
    private Long agencyId;
    private Long participantId;
    private Long programId;
    private String message;
    private RemarkBy sentBy;
    private NotificationType notificationType;
    private Boolean isRead;
}
