package com.metaverse.workflow.tgtpc_handholding.response_dto;

import com.metaverse.workflow.aleap_handholding.service.Participants;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestingQualityCertificationSupportResponse {
    private Long id;
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
    private List<Participants> participants;
    private String handholdingDate;
    private String handholdingTime;
}
