package com.metaverse.workflow.aleap_handholding.request_dto;

import com.metaverse.workflow.model.aleap_handholding.TradeFairParticipation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeFairParticipationRequest {

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String handHoldingType;

    private TradeFairParticipation.EventType eventType;
    private String eventDate;
    private String eventLocation;
    private String organizedBy;
}
