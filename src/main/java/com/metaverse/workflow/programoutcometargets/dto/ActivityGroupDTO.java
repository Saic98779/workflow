package com.metaverse.workflow.programoutcometargets.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityGroupDTO {
    private List<String> financialYearHeaders;
    private List<FinancialTargetSummaryDTO> financialYears;
}
