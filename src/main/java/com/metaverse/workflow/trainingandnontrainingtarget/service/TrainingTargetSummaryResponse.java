package com.metaverse.workflow.trainingandnontrainingtarget.service;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrainingTargetSummaryResponse {
    private List<String> financialYearHeaders;
    private Double overallTarget;
    private List<TrainingTargetResponse> financialYear;
}
