package com.metaverse.workflow.email;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class EmailNotificationController {

    private final EmailEventProducer producerService;

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest request) {

        EmailEvent event = new EmailEvent(
                request.getTo(),
                request.getSubject(),
                request.getBody(),
                request.isHtml()
        );

        producerService.sendEmail(event);

        return ResponseEntity.ok("Email event published successfully");
    }
}

