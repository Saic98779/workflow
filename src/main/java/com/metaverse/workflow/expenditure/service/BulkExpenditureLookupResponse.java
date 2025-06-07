package com.metaverse.workflow.expenditure.service;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class BulkExpenditureLookupResponse {

    private String itemName;
    private Long bulkExpenditureTransactionId;
    private Integer purchasedQuantity;
    private Double unitCost;
    private Long bulkExpenditureId;
    private String purchaseDate;
    private Integer consumedQuantity;
    private Integer availableQuantity;
}
