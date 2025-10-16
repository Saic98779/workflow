package com.metaverse.workflow.notifications.dto;

import com.metaverse.workflow.enums.NotificationRecipientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationRequestDto  {
    private String callCenterUserId;
    private Long agencyId;
    private String message;
    private Long participantId;
    private Long programId;
}
