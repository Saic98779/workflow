package com.metaverse.workflow.notifications.dto;

import com.metaverse.workflow.model.GlobalNotifications;

public class GlobalNotificationMapper {
    public static GlobalNotificationDTO toDto(GlobalNotifications notification) {
        if (notification == null) return null;
        return GlobalNotificationDTO.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .remarkBy(notification.getRemarkBy())
                .recipientType(notification.getRecipientType())
                .isRead(notification.getIsRead())
                .dateOfNotification(notification.getDateOfNotification().toString())
                .createdById(notification.getCreatedBy() != null ? notification.getCreatedBy().getUserId() : null)
                .recipientUserId(notification.getRecipientUser() != null ? notification.getRecipientUser().getUserId() : null)
                .agencyId(notification.getAgency() != null ? notification.getAgency().getAgencyId() : null)
                .build();
    }
}
