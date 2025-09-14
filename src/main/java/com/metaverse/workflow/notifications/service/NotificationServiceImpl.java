package com.metaverse.workflow.notifications.service;

import com.metaverse.workflow.common.enums.UserRole;
import com.metaverse.workflow.enums.UserType;
import com.metaverse.workflow.model.Notifications;
import com.metaverse.workflow.notifications.dto.NotificationReadUpdateDto;
import com.metaverse.workflow.notifications.dto.NotificationRequest;
import com.metaverse.workflow.notifications.dto.NotificationResponse;
import com.metaverse.workflow.notifications.dto.NotificationResponseDto;
import com.metaverse.workflow.notifications.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Async
    @Override
    public CompletableFuture<NotificationResponse> saveNotification(NotificationRequest request) {
        Notifications notifications = Notifications.builder()
                .userType(UserType.valueOf(request.getUserType()))
                .sourceId(request.getSourceId())
                .screenName(request.getScreenName())
                .message(request.getMessage())
                .userId(request.getUserId())
                .readRecipients(request.getReadRecipients())
                .build();
        Notifications savedNotification = notificationRepository.save(notifications);
        NotificationResponse response = NotificationResponse.builder()
                .id(savedNotification.getId())
                .userType(String.valueOf(savedNotification.getUserType()))
                .sourceId(savedNotification.getSourceId())
                .screenName(savedNotification.getScreenName())
                .message(savedNotification.getMessage())
                .userId(savedNotification.getUserId())
                .readRecipients(savedNotification.getReadRecipients())
                .build();
        return CompletableFuture.completedFuture(response);
    }

    @Override
    public NotificationResponseDto getAllNotifications() {
        return null;
    }

    @Override
    public NotificationResponseDto getAllNotificationsByUserType(UserRole userRole) {
        return null;
    }

    @Override
    @Transactional
    public int markAsRead(List<NotificationReadUpdateDto> dtos) {
        int updatedCount = 0;
        for (NotificationReadUpdateDto dto : dtos) {
            notificationRepository.findById(dto.getId()).ifPresent(notification -> {
                notification.setReadRecipients(true);
                notificationRepository.save(notification);
            });
            updatedCount++;
        }
        return updatedCount;
    }

    @Override
    public List<NotificationResponse> getAllNotificationsByUserId(String userId) {
        List<Notifications> notifications = notificationRepository.findByUserId(userId);

        return notifications.stream()
                .map(n -> NotificationResponse.builder()
                        .id(n.getId())
                        .userType(n.getUserType() != null ? n.getUserType().name() : null)
                        .sourceId(n.getSourceId())
                        .screenName(n.getScreenName())
                        .message(n.getMessage())
                        .userId(n.getUserId())
                        .readRecipients(n.getReadRecipients())
                        .build())
                .toList();
    }

}
