package com.metaverse.workflow.expenditure.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HeadWiseExpenditureSummary {
    private String headOfExpense;
    private Double totalCost;
    private List<CombinedExpenditure> expenditures;
}
