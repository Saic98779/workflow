package com.metaverse.workflow.aleap_handholding.response_dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Builder
public class MarketStudyResponse {
    private Long marketStudyId;
    private String dateOfStudy;
    private Long handholdingSupportId;
    private Long organizationId;
    private String organizationName;
    private String counselledBy;
    private String counsellingDate;
    private String counsellingTime;
    private List<String> participantNames;
    private Long nonTrainingActivityId;
    private String nonTrainingActivityName;
    private Long nonTrainingSubActivityId;
    private String nonTrainingSubActivityName;
    private String handHoldingType;

}
