package com.metaverse.workflow.tgtpc_handholding.response_dto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TGTPCNTReportsResponse {
    private Long reportId;
    private String sectorName;
    private String productName;
    private String reportSubmissionDate;
    private String approvalDate;
    private Long nonTrainingActivityId;
    private String nonTrainingActivityName;
    private Long nonTrainingSubActivityId;
    private String nonTrainingSubActivityName;
}