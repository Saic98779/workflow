package com.metaverse.workflow.dto;

import lombok.Data;

@Data
public class StateRAMPDashbrdDataDto {
    private String intervention;
    private String Component;
    private String Activity;
    private String Year;
    private String Quarter;
    private Double PhysicalTarget;
    private Double PhysicalAchieved;
    private Double FinancialTarget;
    private Double FinancialAchieved;
    private Integer MSMEsBenefittedTotal;
    private Integer MSMEsBenefittedWoman;
    private Integer MSMEsBenefittedSC;
    private Integer MSMEsBenefittedST;
    private Integer MSMEsBenefittedOBC;
}
