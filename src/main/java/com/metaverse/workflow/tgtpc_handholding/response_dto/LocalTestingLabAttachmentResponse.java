package com.metaverse.workflow.tgtpc_handholding.response_dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalTestingLabAttachmentResponse {
    private String labOrCfcName;
    private String purposeOfAttachment;
    private String dateOfAttachment;

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
