package com.metaverse.workflow.programoutcometargets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancialTargetOverAllDTO {
   private String overallTarget;
   private List<FinancialTargetSummaryDTO> financialTargetSummaryDTO;
}
