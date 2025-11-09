package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreadMarkDTO {
    private Long id;
    private String nameOfTradMark;
    private String trademarkClass;
    private String tradeMarkRegistrationNo;
    private Date dateOfRegistration;
    private String jurisdictionCovered;
    private Double annualRevenueAfterRegistration;
    private Date dateOfExport;
    private Double valueOfExport;
    private String countryOfExport;
    private String retailPartnership;
    private Double valueOfSupply;
    private Date dateOfSupply;
    private Integer totalJobsCreated;
    private Integer noOfFranchiseOutletsOpened;
    private Double annualRoyaltyEarningsFromFranchise;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

