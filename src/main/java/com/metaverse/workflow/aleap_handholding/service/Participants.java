package com.metaverse.workflow.aleap_handholding.service;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Participants {
    private Long participantId;
    private Long influencedParticipantId;
    private String participantName;
}
