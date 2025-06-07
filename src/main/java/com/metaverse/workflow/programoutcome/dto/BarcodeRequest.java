package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BarcodeRequest {
    public String industry;
    public String location;
    public String barCodeType;
    public String gs1RegistrationNumber;
    public String barCodeCoverage;//dropdown
    public Double revenueFromBarCodeIntegration;
    public String onlineMarketRegistered;
    public String dateOfRegistration;
    public Double valueOfTransaction;
    public String dateOfExport;
    public Double valueOfExport;
    public String countryExported;
    public Boolean isInfluenced;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;
}
