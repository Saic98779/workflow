package com.metaverse.workflow.email;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    private final JavaMailSender mailSender;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(EmailEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(event.getTo());
            helper.setSubject(event.getSubject());
            helper.setText(event.getBody(), event.isHtml());

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingException("Email sending failed", e);
        }
    }
}
