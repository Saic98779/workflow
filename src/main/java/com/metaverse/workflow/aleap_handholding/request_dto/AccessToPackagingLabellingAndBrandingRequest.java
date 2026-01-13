package com.metaverse.workflow.aleap_handholding.request_dto;

import com.metaverse.workflow.model.aleap_handholding.AccessToPackagingLabellingAndBranding;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessToPackagingLabellingAndBrandingRequest {
    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private Long organizationId;
    private String handHoldingType;
    private String accessToPackagingType;
    private String studioAccessDate;
    private String details;
    private AccessToPackagingLabellingAndBranding.EventType eventType;
    private String eventDate;
    private String eventLocation;
    private String organizedBy;
}
