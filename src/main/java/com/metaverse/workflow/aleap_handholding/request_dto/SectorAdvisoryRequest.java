package com.metaverse.workflow.aleap_handholding.request_dto;

import lombok.Data;

import java.util.List;

@Data
public class SectorAdvisoryRequest {
    private String adviseDetails;
    private Long organizationId;
    private String counselledBy;

    private Long handholdingSupportId;
    private List<Long> participantIds;
    private List<Long> influencedParticipantIds;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String handHoldingType;
    private String counsellingDate;
    private String counsellingTime;
}

