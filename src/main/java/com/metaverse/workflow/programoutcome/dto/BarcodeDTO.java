package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarcodeDTO {
    private Long id;
    private String typeOfMarket;
    private String barCodeType;
    private String gs1RegistrationNumber;
    private String barCodeCoverage;
    private Double revenueFromBarCodeIntegration;
    private String onlineMarketRegistered;
    private Date dateOfRegistration;
    private Double valueOfTransaction;
    private Date dateOfExport;
    private Double valueOfExport;
    private String countryExported;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;

    private Date createdOn;
    private Date updatedOn;
}

