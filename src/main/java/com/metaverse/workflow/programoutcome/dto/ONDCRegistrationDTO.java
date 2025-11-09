package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ONDCRegistrationDTO {
    private Long id;
    private String ondcRegistrationNo;
    private Date ondcRegistrationDate;
    private Boolean isInfluenced;
    private String agencyName;
    private String participantName;
    private String organizationName;
    private Long mobileNo;
    private String influencedParticipantName;
    private Date createdOn;
    private Date updatedOn;
}
