package com.metaverse.workflow.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailEventConsumer {

    private final EmailSender emailSender;

    public EmailEventConsumer(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @KafkaListener(topics = "email-events")
    public void consume(EmailEvent event) {
        log.info("Received email event: {}", event.getTo());
        emailSender.send(event);
    }
}
