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
    private Long agencyId;
    public SubActivityParticipantCountDTO(Long subActivityId, Long participantCount) {
        this.subActivityId = subActivityId;
        this.participantCount = participantCount;
    }
}
