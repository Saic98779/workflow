package com.metaverse.workflow.email.util;

import com.metaverse.workflow.email.EmailRequest;
import com.metaverse.workflow.email.entity.EmailConfiguration;
import com.metaverse.workflow.model.Program;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class EmailUtil {

    /**
     * Build an EmailRequest using the program status template.
     * Made public static for reuse across the codebase.
     */
    public static EmailRequest getEmailRequest(String status, Program program, String actionRequired, EmailConfiguration emailConfiguration) throws IOException {
        String htmlTemplate = new String(
                Objects.requireNonNull(
                        EmailUtil.class.getClassLoader()
                                .getResourceAsStream(
                                        "templates/program-status-notification.html"
                                )
                ).readAllBytes(),
                StandardCharsets.UTF_8
        );


        String emailBody = htmlTemplate
                .replace("{{PROGRAM_NAME}}", program != null && program.getProgramTitle() != null ? program.getProgramTitle() : "")
                .replace("{{CURRENT_STATUS}}", status != null ? status : "")
                .replace("{{ACTION_REQUIRED}}", actionRequired != null ? actionRequired : "");

        EmailRequest emailRequest = EmailRequest.builder()
                .to(emailConfiguration != null ? emailConfiguration.getOperationalToEmailAddresses() : null)
                .cc(emailConfiguration != null ? emailConfiguration.getOperationalCcEmailAddresses() : null)
                .subject("Program Status Update Notification " + "- Program Name: " + (program != null && program.getProgramTitle() != null ? program.getProgramTitle() : ""))
                .body(emailBody)
                .html(true)
                .build();
        return emailRequest;
    }

}
