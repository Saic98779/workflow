package com.metaverse.workflow.notifications.service;

import com.metaverse.workflow.common.enums.UserRole;
import com.metaverse.workflow.notifications.dto.NotificationReadUpdateDto;
import com.metaverse.workflow.notifications.dto.NotificationRequest;
import com.metaverse.workflow.notifications.dto.NotificationResponse;
import com.metaverse.workflow.notifications.dto.NotificationResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface NotificationService {

     CompletableFuture<NotificationResponse> saveNotification(NotificationRequest request);

     NotificationResponseDto getAllNotifications();

     NotificationResponseDto getAllNotificationsByUserType(UserRole userRole);

     int markAsRead(List<NotificationReadUpdateDto> dtos);
}
