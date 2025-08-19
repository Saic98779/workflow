package com.metaverse.workflow.trainingtarget.service;


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
    private Double q1;
    private Double q2;
    private Double q3;
    private Double q4;
    private Double yearlyTarget;
}
