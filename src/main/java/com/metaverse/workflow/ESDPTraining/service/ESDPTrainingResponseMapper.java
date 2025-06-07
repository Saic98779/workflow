package com.metaverse.workflow.ESDPTraining.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.ESDPTraining;

public class ESDPTrainingResponseMapper {

    public static ESDPTrainingResponse map(ESDPTraining esdpTraining)
    {
        return ESDPTrainingResponse.builder()
                .ESDPTrainingApplicationId(esdpTraining.getESDPTrainingApplicationId())
                .SHGName(esdpTraining.getOrganizationId().getNameOfTheSHG())
                .memberId(esdpTraining.getParticipantId().getMemberId())
                .memberName(esdpTraining.getParticipantId().getParticipantName())
                .SHGSector(esdpTraining.getOrganizationId().getSectors())
                .dateOfAwarenessProgram(DateUtil.dateToString(esdpTraining.getDateOfAwarenessProgram(),"dd-MM-yyyy"))
                .interestedInAttending15Days(esdpTraining.getInterestedInAttending15Days())
                .dateOfApplicationReceived(DateUtil.dateToString(esdpTraining.getDateOfApplicationReceived(),"dd-MM-yyyy"))
                .selectedForTraining(esdpTraining.getSelectedForTraining())
                .build();
    }
}
