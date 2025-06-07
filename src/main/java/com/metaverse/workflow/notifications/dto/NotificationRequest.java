package com.metaverse.workflow.notifications.dto;

import com.metaverse.workflow.enums.UserType;
import lombok.Data;

@Data
public class NotificationRequest {
    private Long programId;
    private Long callCenterId;
    private UserType userType;
    private String message;
}