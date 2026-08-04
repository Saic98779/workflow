package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyReportResponseDTO {

    private Long surveyReportId;

    private Long nonTrainingActivityId;

    private String nonTrainingActivityName;

    private Long nonTrainingSubActivityId;

    private String nonTrainingSubActivityName;

    private String reportSubmissionDate;

    private String approvalDate;

    private String reportFile;
}