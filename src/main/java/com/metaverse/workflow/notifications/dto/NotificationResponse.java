package com.metaverse.workflow.notifications.dto;

import lombok.Data;

@Data
public class NotificationResponse {
    private Long id;
    private Long programId;
    private Long callCenterId;
    private String message;
}
