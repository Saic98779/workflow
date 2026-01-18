package com.metaverse.workflow.aleap_handholding.response_dto;

import com.metaverse.workflow.aleap_handholding.service.Participants;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounsellingResponse {
    private Long counselletingId;
    private Long handholdingSupportId;
    private Long organizationId;
    private String organizationName;
    private String counselledBy;
    private String counsellingDate;
    private String counsellingTime;
    private String subjectDelivered;
    private String originalIdea;
    private String finalIdea;
    private List<Participants> participants;
    private Long nonTrainingActivityId;
    private String nonTrainingActivityName;
    private Long nonTrainingSubActivityId;
    private String nonTrainingSubActivityName;
    private String handHoldingType;

}
