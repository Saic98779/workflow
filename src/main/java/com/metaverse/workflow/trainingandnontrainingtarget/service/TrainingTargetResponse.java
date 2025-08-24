package com.metaverse.workflow.trainingandnontrainingtarget.service;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainingTargetResponse {

    private Long trainingTargetId;
    private String agencyName;
    private String activityName;
    private String financialYear;
    private Integer q1;
    private Integer q2;
    private Integer q3;
    private Integer q4;
    private Integer yearlyTarget;
}
