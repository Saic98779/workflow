package com.metaverse.workflow.ESDPTraining.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.*;

public class ESDPTrainingRequestMapper
{
    public static ESDPTraining map(ESDPTrainingRequest request, Organization organization,
                                   Participant participant, Agency agency)
    {
        return ESDPTraining.builder()
                .dateOfAwarenessProgram(DateUtil.stringToDate(request.getDateOfAwarenessProgram(),"dd-MM-yyyy"))
                .selectedForTraining(request.getSelectedForTraining())
                .interestedInAttending15Days(request.getInterestedInAttending15Days())
                .dateOfApplicationReceived(DateUtil.stringToDate(request.getDateOfApplicationReceived(),"dd-MM-yyyy"))
                .participantId(participant)
                .organizationId(organization)
                .agency(agency)
                .build();
    }
}
