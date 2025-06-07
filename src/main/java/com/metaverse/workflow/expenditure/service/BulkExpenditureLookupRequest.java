package com.metaverse.workflow.expenditure.service;

import lombok.Data;

@Data
public class BulkExpenditureLookupRequest {
    private Integer expenseId;
    private String itemName;
}
