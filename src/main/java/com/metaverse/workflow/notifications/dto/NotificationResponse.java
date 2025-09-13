package com.metaverse.workflow.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationResponse {
    private Long id;
    private String applicationNo;
    private String applicationStatus;
    private String userType;
    private Long   sourceId;           // sourceId
    private String screenName;      //Screen name
    private String message;
    private String userId;
    private String managerId;
    private Boolean readRecipients;
    private String district;
}