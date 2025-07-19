package com.metaverse.workflow.expenditure.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenditureRemarksDTO {

    private String userId;

    private String spiuComments;

    private String agencyComments;

    private Long expenditureId;

    private Long transactionId;
}

