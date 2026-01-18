package com.metaverse.workflow.aleap_handholding.response_dto;

import com.metaverse.workflow.aleap_handholding.service.Participants;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SectorAdvisoryResponse {
    private Long sectorAdvisoryId;
    private Long handholdingSupportId;
    private Long organizationId;
    private String organizationName;
    private String counselledBy;
    private String counsellingDate;
    private String counsellingTime;
    private String adviseDetails;
    private Long nonTrainingActivityId;
    private String nonTrainingActivityName;
    private Long nonTrainingSubActivityId;
    private String nonTrainingSubActivityName;
    private String handHoldingType;
    private List<Participants> participants;
}

