package com.metaverse.workflow.notifications.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationReadUpdateDto {
    @NotNull
    private Long id;
}

