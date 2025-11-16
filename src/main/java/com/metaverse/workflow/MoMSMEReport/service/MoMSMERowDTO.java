package com.metaverse.workflow.MoMSMEReport.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoMSMERowDTO {

    private String activityName;
    private String subActivityName;
    private Long totalTarget;
    private Integer achievementLastMonth;
    private Integer achievementDuringMonth;
    private Integer cumulativeAchievement;
    private String achievementPercentage;
    private Integer msmesBenefited;
    private Double amountApproved;
    private Double expenditureLastMonth;
    private Double expenditureDuringMonth;
    private Double cumulativeExpenditure;
    private String expenditurePercentage;
}
