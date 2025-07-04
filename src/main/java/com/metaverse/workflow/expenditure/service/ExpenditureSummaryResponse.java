package com.metaverse.workflow.expenditure.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class ExpenditureSummaryResponse {
    private List<HeadWiseExpenditureSummary> summaries;
    private Double grandTotal;
}
