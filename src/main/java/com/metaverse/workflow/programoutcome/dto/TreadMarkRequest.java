package com.metaverse.workflow.programoutcome.dto;

import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TreadMarkRequest {
    public String nameOfTradMark;
    public String trademarkClass;
    public String tradeMarkRegistrationNo;
    public String dateOfRegistration;
    public String jurisdictionCovered;
    public Double annualRevenueAfterRegistration;
    public String dateOfExport;
    public Double valueOfExport;
    public String countryOfExport;
    public String retailPartnership;
    public Double valueOfSupply;
    public String dateOfSupply;
    public Integer totalJobsCreated;
    public Integer noOfFranchiseOutletsOpened;
    public Double annualRoyaltyEarningsFromFranchise;
    public Boolean isInfluenced;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;
    
}
