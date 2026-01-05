package com.metaverse.workflow.email;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailEventProducer {

    private final KafkaTemplate<String, EmailEvent> kafkaTemplate;

    public EmailEventProducer(@Qualifier("emailEventKafkaTemplate") KafkaTemplate<String, EmailEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmail(EmailEvent event) {
        kafkaTemplate.send("email-events", event);
    }
}
