package com.metaverse.workflow.notifications.dto;

import com.metaverse.workflow.enums.RemarkBy;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GlobalNotificationRequest {

    private String userId;        // Receiver (assignee / agent / admin)
    private Long agencyId;
    private Long participantId;
    private Long programId;

    private String message;       // the content
    private RemarkBy sentBy;      // CALL_CENTER / AGENCY / ADMIN
}

