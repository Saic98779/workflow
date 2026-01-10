package com.metaverse.workflow.tgtpc_handholding.request_dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TGTPCNTReportsRequest {
    private String sectorName;
    private String productName;
    private String reportSubmissionDate;
    private String approvalDate;
    private Long nonTrainingSubActivityId;
}