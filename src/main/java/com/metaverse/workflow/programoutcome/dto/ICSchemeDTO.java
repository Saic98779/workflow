package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ICSchemeDTO {
    private Long id;
    private String industryName;
    private String location;
    private String typeOfMsme;
    private Double annualTurnover;
    private Double domesticSales;
    private Double investment;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

