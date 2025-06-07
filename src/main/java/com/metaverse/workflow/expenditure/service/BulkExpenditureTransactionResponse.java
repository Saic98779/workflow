package com.metaverse.workflow.expenditure.service;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BulkExpenditureTransactionResponse {
    private Long id;
    private Integer consumedQuantity;
    private Double allocatedCost;

}
