package com.metaverse.workflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StateRAMPDashbrdDataDto {

    @JsonProperty("Intervention")
    private String intervention;

    @JsonProperty("Component")
    private String component;

    @JsonProperty("Activity")
    private String activity;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Quarter")
    private String quarter;

    @JsonProperty("PhysicalTarget")
    private Double physicalTarget;

    @JsonProperty("PhysicalAchieved")
    private Double physicalAchieved;

    @JsonProperty("FinancialTarget")
    private Double financialTarget;

    @JsonProperty("FinancialAchieved")
    private Double financialAchieved;

    @JsonProperty("MSMEsBenefittedTotal")
    private Integer msmesBenefittedTotal;

    @JsonProperty("MSMEsBenefittedWoman")
    private Integer msmesBenefittedWoman;

    @JsonProperty("MSMEsBenefittedSC")
    private Integer msmesBenefittedSC;

    @JsonProperty("MSMEsBenefittedST")
    private Integer msmesBenefittedST;

    @JsonProperty("MSMEsBenefittedOBC")
    private Integer msmesBenefittedOBC;
}
