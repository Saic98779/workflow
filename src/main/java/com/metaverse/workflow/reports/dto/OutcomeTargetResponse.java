package com.metaverse.workflow.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutcomeTargetResponse {
    private int grandTargetTotal;
    private int grandAchievedTotal;
    private List<OutcomeTargetDTO> outcomeTargetDTOList;
}
