package com.metaverse.workflow.notifications.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.notifications.dto.GlobalNotificationRequest;
import com.metaverse.workflow.notifications.dto.NotificationDto;
import com.metaverse.workflow.notifications.service.NotificationServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    // =========================================================================
    // 1. GET NOTIFICATIONS BY ROLE (ADMIN, CALL_CENTER, AGENCY_ADMIN, etc.)
    // =========================================================================
    @GetMapping("/role/{role}")
    public ResponseEntity<WorkflowResponse> getNotificationsByRole(@PathVariable String role) {

        NotificationDto notifications = notificationService.getNotificationsByRole(role);

        return ResponseEntity.ok(
                WorkflowResponse.success("Notifications fetched successfully", notifications)
        );
    }

    // =========================================================================
    // 2. GET NOTIFICATIONS FOR SPECIFIC USER (Receiver)
    // =========================================================================
    @GetMapping("/user/{userId}")
    public ResponseEntity<WorkflowResponse> getNotificationsForUser(@PathVariable String userId) {

        List<NotificationDto> notifications = notificationService.getNotificationsForUser(userId);

        return ResponseEntity.ok(
                WorkflowResponse.success("User notifications fetched successfully", notifications)
        );
    }

    // =========================================================================
    // 3. GET SINGLE NOTIFICATION BY ID
    // =========================================================================
    @GetMapping("/{id}")
    public ResponseEntity<WorkflowResponse> getNotificationById(@PathVariable Long id) {

        NotificationDto notification = notificationService.getNotificationById(id);

        return ResponseEntity.ok(
                WorkflowResponse.success("Notification details fetched", notification)
        );
    }

    // =========================================================================
    // 4. SEND / CREATE A NEW NOTIFICATION (ASYNC)
    // =========================================================================
    @PostMapping("/send")
    public ResponseEntity<WorkflowResponse> sendNotification(@RequestBody GlobalNotificationRequest req) {

        notificationService.saveNotification(req); // async

        return ResponseEntity.ok(
                WorkflowResponse.success("Notification sent successfully")
        );
    }

    // =========================================================================
    // 5. MARK AS FIXED
    // =========================================================================
    @PutMapping("/{id}/fix")
    public ResponseEntity<WorkflowResponse> markAsFixed(@PathVariable Long id) {

        notificationService.markAsFixed(id);

        return ResponseEntity.ok(
                WorkflowResponse.success("Notification marked as FIXED")
        );
    }

    // =========================================================================
    // 6. MARK AS CLOSED
    // =========================================================================
    @PutMapping("/{id}/close")
    public ResponseEntity<WorkflowResponse> markAsClosed(@PathVariable Long id) {

        notificationService.markAsClosed(id);

        return ResponseEntity.ok(
                WorkflowResponse.success("Notification marked as CLOSED")
        );
    }
}
