package com.metaverse.workflow.tgtpc_handholding.response_dto;

import com.metaverse.workflow.aleap_handholding.service.Participants;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialSupportResponse {
    private Long id;
    private String rawMaterialDetails;
    private String firstDateOfSupply;
    private Double cost;

    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingActivityName;
    private String nonTrainingSubActivityName;
    private String handholdingSupportBy;
    private Long organizationId;
    private String organizationName;
    private List<Participants> participants;
    private String handholdingDate;
    private String handholdingTime;
}
