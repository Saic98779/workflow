package com.metaverse.workflow.aleap_handholding.response_dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TradeFairParticipationResponse {

    private Long tradeFairParticipationId;

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingActivityName;
    private String nonTrainingSubActivityName;

    private String eventType;
    private String eventDate;
    private String eventLocation;
    private String organizedBy;
}
