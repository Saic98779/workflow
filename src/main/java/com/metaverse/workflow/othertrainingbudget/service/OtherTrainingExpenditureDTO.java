package com.metaverse.workflow.othertrainingbudget.service;

import lombok.Data;

@Data
public class OtherTrainingExpenditureDTO {
    private Long id;
    private String dateOfExpenditure;
    private String details;
    private Double amount;
    private String billPath;
    private String billNo;
    private String billDate;
    private Long budgetId;
}
