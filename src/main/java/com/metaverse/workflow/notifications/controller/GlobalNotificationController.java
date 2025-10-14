package com.metaverse.workflow.notifications.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.enums.NotificationRecipientType;
import com.metaverse.workflow.notifications.dto.GlobalNotificationDTO;
import com.metaverse.workflow.notifications.service.GlobalNotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/global-notifications")
@RestController
@RequiredArgsConstructor
public class GlobalNotificationController {
    private final GlobalNotificationServiceImpl notificationService;

    @PostMapping("/send")
    public ResponseEntity<WorkflowResponse> send(@RequestBody GlobalNotificationDTO dto) {
        return ResponseEntity.ok(notificationService.sendNotification(dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WorkflowResponse> getUserNotifications(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userId));
    }

    @GetMapping("/recipient/{type}")
    public ResponseEntity<WorkflowResponse> getByRecipientType(
            @PathVariable NotificationRecipientType type,
            @RequestParam(required = false) Long agencyId) {
        return ResponseEntity.ok(notificationService.getNotificationsForRecipientType(type, agencyId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<WorkflowResponse> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }
}
