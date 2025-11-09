package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PMMYDTO {
    private Long id;
    private String category;
    private Double loanAmountReleased;
    private Date loanSanctionedDate;
    private Date groundingDate;
    private Double businessTurnover;
    private Date marketLinkageDate;
    private Double marketVolume;
    private String units;
    private Double marketValue;
    private String productMarketedName;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

