package com.metaverse.workflow.notifications.service;

import com.metaverse.workflow.common.enums.UserRole;
import com.metaverse.workflow.model.Notifications;
import com.metaverse.workflow.notifications.dto.NotificationReadUpdateDto;
import com.metaverse.workflow.notifications.dto.NotificationRequest;
import com.metaverse.workflow.notifications.dto.NotificationResponse;
import com.metaverse.workflow.notifications.dto.NotificationResponseDto;
import com.metaverse.workflow.notifications.repository.NotificationRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Async
    public CompletableFuture<NotificationResponse> saveNotification(NotificationRequest request) {

        Notifications notification = Notifications.builder()
                .applicationNo(request.getApplicationNo())
                .userType(request.getUserType())
                .message(request.getMessage())
                .sourceId(request.getSourceId())
                .screenName(request.getScreenName())
                .userId(request.getUserId())
                .managerId(request.getManagerId())
                .applicationStatus(request.getApplicationStatus())
                .readRecipients(request.getReadRecipients())
                .district(request.getDistrict())
                .build();
        Optional<Notifications> byApplicationStatusAndApplicationNo = notificationRepository.findByApplicationNo(request.getApplicationNo());

        Notifications saved;
        if (byApplicationStatusAndApplicationNo.isPresent()) {
            Notifications notifications = byApplicationStatusAndApplicationNo.get();
            notifications.setApplicationNo(request.getApplicationNo());
            notifications.setUserType(request.getUserType());
            notifications.setMessage(request.getMessage());
            notifications.setSourceId(request.getSourceId());
            notifications.setScreenName(request.getScreenName());
            notifications.setUserId(notifications.getUserId());
            notifications.setManagerId(notifications.getManagerId());
            notifications.setApplicationStatus(request.getApplicationStatus());
            notifications.setReadRecipients(request.getReadRecipients());
            notifications.setDistrict(notifications.getDistrict());
            saved = notificationRepository.save(notifications);
        } else {
            saved = notificationRepository.save(notification);
        }
        NotificationResponse response = new NotificationResponse();
        response.setApplicationNo(saved.getApplicationNo());
        response.setApplicationStatus(saved.getApplicationStatus());
        response.setUserType(saved.getUserType());
        response.setScreenName(saved.getScreenName());
        response.setMessage(saved.getMessage());
        response.setUserId(saved.getUserId());
        response.setManagerId(saved.getManagerId());
        response.setReadRecipients(saved.getReadRecipients());
        response.setDistrict(saved.getDistrict());
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
    public int markAsRead(List<NotificationReadUpdateDto> dtos) {
        return 0;
    }
}
