package com.metaverse.workflow.programoutcome.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutComesTargetsAndAchiDto {

    private String agencyName;
    private String outComeName;
    private Long outComeTarget;
    private Long outComeParticipantAchi;
    private Long outComeInfluencerAchi;
}
