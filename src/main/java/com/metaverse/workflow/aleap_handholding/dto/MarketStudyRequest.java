package com.metaverse.workflow.aleap_handholding.dto;

import lombok.Data;

import java.util.List;

@Data
public class MarketStudyRequest {

    private String dateOfStudy;
    private List<FeasibilityInputRequest> inputs;

    private Long handholdingSupportId;
    private Long organizationId;
    private String counselledBy;
    private List<Long> participantIds;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingAction;
    private String counsellingDate;
    private String counsellingTime;
}
