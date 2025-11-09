package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OEMDTO {
    private Long id;
    private Date oemRegistrationDate;
    private String oemRegistrationNumber;
    private String oemTargeted;
    private String oemVendorCode;
    private String productsSupplied;
    private Date vendorRegistrationDate;
    private Date firstPurchaseOrderDate;
    private Double firstPOValue;
    private Double currentMonthlySupplyValue;
    private Boolean isCertificationStatus;
    private String machineryUpGradation;
    private String oemAuditScore;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}
