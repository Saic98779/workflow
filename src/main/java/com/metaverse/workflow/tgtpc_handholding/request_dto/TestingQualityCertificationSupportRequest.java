package com.metaverse.workflow.tgtpc_handholding.request_dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestingQualityCertificationSupportRequest {
    private TGTPCHandholdingSupportRequest tgtpcHandholdingSupportRequest;
    private String testingAgencyName;
    private String dateOfTest;
    private String productTested;
    private String testResultsDate;
    private String certificationOrTestFindings;
}

