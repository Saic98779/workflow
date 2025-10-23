package com.metaverse.workflow.participant.service;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ParticipantsOrInfluencedParticipant {
    private Long organizationId;
    private String organizationName;
    private String nameOfTheSHG;
    private Long influencedId;
    private Long participantId;
    private String participantName;
    private Long mobileNo;
    private String designation;
    private List<String> programDates;
}
