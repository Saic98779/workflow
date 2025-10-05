package com.metaverse.workflow.dto;
import lombok.Data;

@Data
public class StateRAMPDashbrdDataDto {
    private String intervention;
    private String component;
    private String activity;
    private String year;
    private String quarter;
    private double physicalTarget;
    private double physicalAchieved;
    private double financialTarget;
    private double financialAchieved;
    private int msmesBenefittedTotal;
    private int msmesBenefittedWoman;
    private int msmesBenefittedSC;
    private int msmesBenefittedST;
    private int msmesBenefittedOBC;
}