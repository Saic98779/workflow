package com.metaverse.workflow.trainingandnontrainingtarget.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TargetResponse {

    private Long targetId;
    private String activityName;
    private String agencyName;
    private String subActivityName;


    private Long physicalTargetQ1;
    private Long physicalTargetQ2;
    private Long physicalTargetQ3;
    private Long physicalTargetQ4;
    private Long totalTrainingTarget;

    private Double financialTargetQ1;
    private Double financialTargetQ2;
    private Double financialTargetQ3;
    private Double financialTargetQ4;
    private Double totalFinancialTarget;
    private String financialYear;

}
