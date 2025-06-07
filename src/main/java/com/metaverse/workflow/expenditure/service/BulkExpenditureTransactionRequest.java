package com.metaverse.workflow.expenditure.service;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class BulkExpenditureTransactionRequest {

    private Long activityId;
    private Long subActivityId;
    private Long programId;
    private Long agencyId;
    private Integer headOfExpenseId;
    private Long bulkExpenditureId;
    private Integer consumedQuantity;
    private Double allocatedCost;
}
