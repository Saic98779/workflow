package com.metaverse.workflow.notifications.service;

import com.metaverse.workflow.notifications.dto.NotificationRequest;
import com.metaverse.workflow.notifications.dto.NotificationResponse;

import java.util.concurrent.CompletableFuture;

public interface NotificationService {
    CompletableFuture<NotificationResponse> saveNotification(NotificationRequest request);
}
