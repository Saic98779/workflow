package com.metaverse.workflow.programoutcome.dto;


import lombok.*;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZEDCertificationRequest {

    public String ownerName;
    public String nicCode;
    public String unitAddress;
    public String certificationDate;
    public String zedCertificationId;
    private String zedCertificationType; // Bronze / Silver / Gold
    public Double turnover;
    public Double energyConsumptionKwhHr;
    public Boolean isInfluenced;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;
    private Long influencedId;

}
