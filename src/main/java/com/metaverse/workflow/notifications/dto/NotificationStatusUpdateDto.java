package com.metaverse.workflow.notifications.dto;

import com.metaverse.workflow.enums.NotificationStatus;
import com.metaverse.workflow.enums.RemarkBy;
import lombok.Data;

@Data
public class NotificationStatusUpdateDto {
    private Long notificationId;
    private NotificationStatus status;
    private String remark;
    private RemarkBy remarkBy;
}

