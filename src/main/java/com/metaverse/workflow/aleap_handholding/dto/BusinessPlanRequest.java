package com.metaverse.workflow.aleap_handholding.dto;

import lombok.Data;

import java.util.List;

@Data
public class BusinessPlanRequest {

    private String planFileUploadPath;
    private String bankName;
    private String branchName;
    private String bankRemarks;
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
