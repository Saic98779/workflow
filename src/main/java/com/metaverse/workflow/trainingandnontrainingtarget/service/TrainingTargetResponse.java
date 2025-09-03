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
    private Long q1;
    private Long q2;
    private Long q3;
    private Long q4;
    private Long yearlyTarget;
}
