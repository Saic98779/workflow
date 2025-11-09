package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NSICDTO {
    private Long id;
    private String govtAgencyProcured;
    private Date dateOfProcurement;
    private String typeOfProcurement;
    private String typeOfProductSupplied;
    private Double valueOfProcurement;
    private Double costSavingsTender;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

