package com.metaverse.workflow.notifications.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.notifications.dto.GlobalNotificationRequest;
import com.metaverse.workflow.notifications.dto.GlobalNotificationResponse;
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

       List<GlobalNotificationResponse>  notifications = notificationService.getNotificationsByRole(role);
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

    @GetMapping("/un-read")
    public ResponseEntity<WorkflowResponse> getNotifications(@RequestParam Long agencyId, @RequestParam Boolean isRead) {

        try{
            List<GlobalNotificationResponse> notifications = notificationService.getAllUnReadNotifications(agencyId, isRead);
            WorkflowResponse.builder().data(notifications).status(200).message("Success").build();
            return ResponseEntity.ok(WorkflowResponse.builder().data(notifications).status(200).message("Success").build());

        }catch (Exception e){
           return ResponseEntity.badRequest().body(WorkflowResponse.builder().message(e.getMessage()).status(400).message("Failed").build());
        }
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<WorkflowResponse> updateIsRead(@PathVariable Long notificationId, @RequestParam Boolean isRead) {
        try{
             notificationService.updateIsRead(notificationId, isRead);
            return ResponseEntity.ok(WorkflowResponse.builder().status(200).message("Success").build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(WorkflowResponse.builder().message(e.getMessage()).status(400).message("Failed").build());
        }
    }
}
