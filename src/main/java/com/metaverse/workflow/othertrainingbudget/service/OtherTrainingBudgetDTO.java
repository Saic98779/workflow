package com.metaverse.workflow.othertrainingbudget.service;

import lombok.Data;

import java.util.List;

@Data
public class OtherTrainingBudgetDTO {
    private Long budgetId;
    private String budgetHead;
    private Double phyTarget;
    private String phyTargetAchievement;
    private Double finTarget;
    private Double finTargetAchievement;
    private Long agencyId;
    private String phyPercentage;
    private String finPercentage;
    private List<OtherTrainingExpenditureDTO> otherTrainingExpenditures;
}
