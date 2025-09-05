package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.Data;

@Data
public class WeHubHandholdingResponse {
    private Long handholdingId;
    private Long organizationId;
    private String organizationName;
    private Boolean statusProductDiversification;
    private String dateSupportReceived;
    private String iprName;
    private String iprFilingDate;
    private Boolean statusTrademarkFiling;
    private String trademarkFilingDate;
    private Boolean statusInventoryManagement;
    private String inventoryAdoptionDate;
    private String leanName;
    private String leanAdoptionDate;
    private Boolean newMachineryAdoption;
    private String establishmentDate;
    private Boolean statusTechnologyRedesign;
    private String technologyRedesignDate;
    private Boolean statusDigitalSolution;
    private String digitalSolutionDate;
    private String innovativeProcessName;
    private String innovativeProcessDate;
    private String skillTrainingName;
    private String skillTrainingPerson;
    private String skillTrainingDate;
    private Long subActivityId;
    private String subActivityName;

}
