package com.metaverse.workflow.aleap_handholding.dto;

import com.metaverse.workflow.model.aleap_handholding.FeasibilityInput;
import lombok.Data;

import java.util.List;

@Data
public class FeasibilityInputRequest {
    private Long marketStudyId;
    private String inputDetails;
    private FeasibilityInput.SourceType source;
    private String sector;
    private String feasibilityActivity;

    private Long organizationId;
    private String counselledBy;
    private List<Long> participantIds;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingAction;
    private String counsellingDate;
    private String counsellingTime;
}
