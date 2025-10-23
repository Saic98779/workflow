package com.metaverse.workflow.programoutcome.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutComesTargetsAndAchievementDto {

    private String agencyName;
    private String outComeName;
    private Long outComeTarget;
    private Long outComeParticipantAchievement;
    private Long outComeInfluencerAchievement;
}
