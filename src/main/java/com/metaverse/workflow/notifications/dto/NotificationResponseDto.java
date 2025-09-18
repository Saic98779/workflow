package com.metaverse.workflow.notifications.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.metaverse.workflow.enums.NotificationRecipientType;
import com.metaverse.workflow.enums.NotificationStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NotificationResponseDto {
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateOfNotification;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateOfFirstNotification;

    private String callCenterAgentName;
    private String agencyName;
    private String participantName;
    private String programName;

    private NotificationStatus status;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateOfFix;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateOfClosure;

    private NotificationRecipientType recipientType;

    // New fields replacing updateInformation
    private List<String> remarksByAgency;
    private List<String> remarksByCallCenter;
}
