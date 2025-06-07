package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GIProductRequest {
    public String companyName;
    public String location;
    public String industry;
    public String giProductName;
    public String giRegistrationNumber;
    public String dateOfGIRegistration;
    public String jurisdictionCovered;
    public Double revenueAfterGICertification;
    public String dateOfExport;
    public Double valueOfExport;
    public String countryExported;
    public String retailPartnership;
    public Double valueOfSupply;
    public String dateOfSupply;
    public Integer totalJobsCreated;
    public Integer franchiseOutletsOpened;
    public Double annualRoyaltyEarnings;
    public Boolean isInfluenced;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;
}
