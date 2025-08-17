package com.metaverse.workflow.programoutcometargets.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinancialTargetOverAllDTO {
   private String overallTarget;
   private Map<String, ActivityGroupDTO> groupedFinancialTargets;
}
