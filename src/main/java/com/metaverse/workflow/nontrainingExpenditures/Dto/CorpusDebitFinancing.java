package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CorpusDebitFinancing {
    private Long registrationUsageId;
    private String enterpriseName;
    private LocalDate createdOn;
    private Double riskCategoryScore;
    private Double totalDisbursedAmount;
    private LocalDate collectionDate;
    private Double sanctionedAmount;
    private LocalDate sanctionedDate;
}
