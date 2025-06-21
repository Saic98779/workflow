package com.metaverse.workflow.programoutcome.dto;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OEMRequest {

    public String oemRegistrationDate;

    public String oemRegistrationNumber;

    public String oemTargeted;

    public String oemVendorCode;

    public String productsSupplied;

    public String vendorRegistrationDate;

    public String firstPurchaseOrderDate;

    public Double firstPOValue;

    public Double currentMonthlySupplyValue;

    public Boolean isCertificationStatus; // Same name as entity

    public String machineryUpGradation;

    public String oemAuditScore;

    public Boolean isInfluenced;

    private Long participantId;
    private Long organizationId;
    private Long agencyId;
}
