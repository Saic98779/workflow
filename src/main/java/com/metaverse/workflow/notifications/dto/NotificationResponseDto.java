package com.metaverse.workflow.notifications.dto;

import lombok.Data;

import java.util.List;

@Data
public class NotificationResponseDto {

    private List<NotificationResponse> notificationResponseList;

    private Integer count;
}

