package com.metaverse.workflow.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemarkDto {
    private String remarksText;
    private String agencyName;
    private String ccName;
    private LocalDateTime remarksDate;
}

