package com.metaverse.workflow.notifications.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GlobalNotificationResponse {

    private Long notificationId;
    private Long agencyId;
    private Long participantId;
    private Long programId;
    private List<NotificationMessageDto> notificationMessageDto;
    private Boolean isRead;
}
