package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyReportDTO {

    private Long surveyReportId;

    private Long nonTrainingActivityId;

    private Long nonTrainingSubActivityId;

    private String reportSubmissionDate;

    private String approvalDate;

    private String reportFile;
}