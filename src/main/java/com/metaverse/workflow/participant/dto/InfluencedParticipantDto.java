package com.metaverse.workflow.participant.dto;

import lombok.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfluencedParticipantDto {

    private Long influencedId;
    private String participantName;
    private Character gender;
    private String category;
    private Character disability;
    private Long aadharNo;
    private Long mobileNo;
    private String email;
    private String designation;
    private Long organizationId;
}

