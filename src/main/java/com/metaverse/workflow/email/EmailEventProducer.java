package com.metaverse.workflow.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailEventProducer {

    private static final Logger log = LoggerFactory.getLogger(EmailEventProducer.class);

    private final EmailSender emailSender;
    private final KafkaTemplate<String, EmailEvent> kafkaTemplate;
    private final boolean kafkaEnabled;

    public EmailEventProducer(EmailSender emailSender,
                              @Qualifier("emailEventKafkaTemplate") @Autowired(required = false) KafkaTemplate<String, EmailEvent> kafkaTemplate,
                              @Value("${app.kafka.enabled:false}") boolean kafkaEnabled) {
        this.emailSender = emailSender;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaEnabled = kafkaEnabled;
    }

    public void sendEmail(EmailEvent event) {
        if (kafkaEnabled && kafkaTemplate != null) {
            kafkaTemplate.send("email-events", event);
        } else {
            emailSender.send(event);
        }
    }

    @Async("taskExecutor")
    public void sendEmailAsync(EmailEvent event) {
        try {
            sendEmail(event);
        } catch (Exception e) {
            log.error("Failed to send email asynchronously: {}", e.getMessage(), e);
        }
    }
}