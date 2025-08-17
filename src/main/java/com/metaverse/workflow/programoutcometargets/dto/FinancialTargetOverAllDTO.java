package com.metaverse.workflow.programoutcometargets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancialTargetOverAllDTO {
   private String overallTarget;
   private Map<String, List<FinancialTargetSummaryDTO>> groupedFinancialTargets;
}
