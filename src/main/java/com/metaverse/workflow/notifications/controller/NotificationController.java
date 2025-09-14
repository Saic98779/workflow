package com.metaverse.workflow.notifications.controller;

import com.metaverse.workflow.common.enums.UserRole;
import com.metaverse.workflow.model.Notifications;
import com.metaverse.workflow.notifications.dto.NotificationReadUpdateDto;
import com.metaverse.workflow.notifications.dto.NotificationRequest;
import com.metaverse.workflow.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<?> saveNotifications(@RequestBody NotificationRequest notifications){
        return  ResponseEntity.ok(notificationService.saveNotification(notifications));
    }

    @GetMapping
    public ResponseEntity<?> getAllNotifications(){
        return  ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/by-user")
    public ResponseEntity<?> getAllNotificationsByUserId(@RequestParam String userId){
        return  ResponseEntity.ok(notificationService.getAllNotificationsByUserId(userId));
    }

    @GetMapping("/userType")
    public ResponseEntity<?> getAllNotificationsByUserType(@RequestParam UserRole userRole){
        return  ResponseEntity.ok(notificationService.getAllNotificationsByUserType(userRole));
    }

    @PatchMapping("/mark-read")
    public ResponseEntity<?> markRead(@RequestBody List<NotificationReadUpdateDto> dtos) {
        int updated = notificationService.markAsRead(dtos);
        return ResponseEntity.ok(Map.of(
                "requested", dtos.stream().map(NotificationReadUpdateDto::getId).toList(),
                "updatedCount", updated
        ));
    }
}

