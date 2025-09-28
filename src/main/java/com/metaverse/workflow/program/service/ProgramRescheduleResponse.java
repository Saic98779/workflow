package com.metaverse.workflow.program.service;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramRescheduleResponse {
    private Long rescheduleId;
    private String oldStartDate;
    private String newStartDate;
    private String oldEndDate;
    private String newEndDate;
    private String rescheduledTimestamp;
    private String programTitle;
}
