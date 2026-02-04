package com.metaverse.workflow.nontrainingExpenditures.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NonTrainingConsumablesTransactionsDTO {

    private Long id;
    private Long programId;
    private String programName;
    private String dateOfUtilisation;
    private Integer quantityOfUtilisation;
    private Integer noOfTraineesUtilised;
    private Long bulkId;
}

