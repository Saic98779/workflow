package com.metaverse.workflow.ESDPTraining.service;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ESDPTrainingRequest {
    private Long participantId;
    private Long organizationId;
    private Long agencyId;
    private String dateOfAwarenessProgram;
    private String interestedInAttending15Days;
    private String dateOfApplicationReceived;
    private String selectedForTraining;
}
