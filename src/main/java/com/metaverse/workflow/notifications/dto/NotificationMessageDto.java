package com.metaverse.workflow.notifications.dto;

import com.metaverse.workflow.common.enums.NotificationType;
import com.metaverse.workflow.enums.RemarkBy;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationMessageDto {

    private Long id;

    private String text;

    private String sentBy;

    private NotificationType notificationType;

    private LocalDateTime createdAt;
}
