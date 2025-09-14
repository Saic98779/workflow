package com.metaverse.workflow.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationRequest {
    private String userType;
    private Long   sourceId;           // sourceId
    private String screenName;      //Screen name
    private String message;
    private String userId;
    private Boolean readRecipients;
}
