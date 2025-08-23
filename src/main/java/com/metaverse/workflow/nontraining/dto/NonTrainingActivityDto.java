package com.metaverse.workflow.nontraining.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class NonTrainingActivityDto {
    private Long activityId;
    private String activityName;
    private String agency;
}