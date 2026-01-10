package com.metaverse.workflow.tgtpc_handholding.response_dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestingQualityCertificationSupportResponse {
    private String testingAgencyName;
    private String dateOfTest;
    private String productTested;
    private String testResultsDate;
    private String certificationOrTestFindings;
    private String testResultFilePath;

    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingActivityName;
    private String nonTrainingSubActivityName;
    private String handholdingSupportBy;
    private Long organizationId;
    private String organizationName;
    private List<String> participantNames;
    private String handholdingDate;
    private String handholdingTime;
}
