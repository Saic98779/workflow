package com.metaverse.workflow.aleap_handholding.response_dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AleapDesignStudioResponse {

    private Long aleapDesignStudioId;

    private Long handholdingSupportId;
    private Long nonTrainingActivityId;
    private Long nonTrainingSubActivityId;
    private String nonTrainingActivityName;
    private String nonTrainingSubActivityName;

    private String studioAccessDate;
    private String details;
    private String aleapDesignStudioImage1;
    private String aleapDesignStudioImage2;
    private String aleapDesignStudioImage3;
}
