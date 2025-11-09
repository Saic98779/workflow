package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZEDCertificationDTO {
    private Long id;
    private String ownerName;
    private String nicCode;
    private String unitAddress;
    private Date certificationDate;
    private String zedCertificationId;
    private String zedCertificationType;
    private Double turnover;
    private Double energyConsumptionKwhHr;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}
