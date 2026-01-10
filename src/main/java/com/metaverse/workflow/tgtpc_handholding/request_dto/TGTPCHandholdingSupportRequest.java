package com.metaverse.workflow.tgtpc_handholding.request_dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TGTPCHandholdingSupportRequest {

    private Long subActivityId;
    private String handholdingSupportBy;
    private Long organizationId;
    private List<Long> participantIds;
    private List<Long> influencedParticipantIds;
    private String handholdingDate;
    private String handholdingTime;
    private String packagingStandardsSupportDetails;// Packaging Standards support
    private String brandingSupportDetails; // Branding Support
}

