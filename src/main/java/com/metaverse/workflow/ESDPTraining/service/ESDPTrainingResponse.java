package com.metaverse.workflow.ESDPTraining.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.metaverse.workflow.model.Sector;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ESDPTrainingResponse {
    private Long ESDPTrainingApplicationId;
    private String SHGName;
    private String memberId;
    private String memberName;
    private List<Sector> SHGSector;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;
    private String dateOfAwarenessProgram;
    private String interestedInAttending15Days;
    private String dateOfApplicationReceived;
    private String selectedForTraining;
}
