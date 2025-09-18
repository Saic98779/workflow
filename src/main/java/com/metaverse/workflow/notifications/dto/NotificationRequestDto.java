package com.metaverse.workflow.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationRequestDto  {
    private Long callCenterUserId;
    private Long agencyId;
    private String message;
}
