package com.metaverse.workflow.aleap_handholding.response_dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessToPackagingLabellingAndBrandingResponse {
    private Long accessToPackagingId;
    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingActivityName;
    private String nonTrainingSubActivityName;
    private String accessToPackagingType;
    private Long organizationId;
    private String organizationName;
    private String studioAccessDate;
    private String details;
    private String aleapDesignStudioImage1;
    private String aleapDesignStudioImage2;
    private String aleapDesignStudioImage3;
    private String eventType;
    private String eventDate;
    private String eventLocation;
    private String organizedBy;

}
