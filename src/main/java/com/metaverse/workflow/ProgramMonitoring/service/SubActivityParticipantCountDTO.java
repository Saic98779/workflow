package com.metaverse.workflow.ProgramMonitoring.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubActivityParticipantCountDTO {
    private Long subActivityId;
    private Long participantCount;
}
