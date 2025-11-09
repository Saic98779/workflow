package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GIProductDTO {
    private Long id;
    private String companyName;
    private String location;
    private String industry;
    private String giProductName;
    private String giRegistrationNumber;
    private Date dateOfGIRegistration;
    private String jurisdictionCovered;
    private Double revenueAfterGICertification;
    private Date dateOfExport;
    private Double valueOfExport;
    private String countryExported;
    private String retailPartnership;
    private Double valueOfSupply;
    private Date dateOfSupply;
    private Integer totalJobsCreated;
    private Integer franchiseOutletsOpened;
    private Double annualRoyaltyEarnings;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;

    private Date createdOn;
    private Date updatedOn;
}

