package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatentsRequest {

    public String nameOfPatent;
    public String typeOfPatent;
    public String patentNumber;
    public String dateOfGrant;
    public String patentCoverage;
    public Double annualRevenue;
    public String dateOfExport;
    public Double valueOfExport;
    public String countryOfExport;
    public Integer totalJobsCreated;
    public String nameOfAward;
    public String dateOfAward;
    public Boolean isInfluenced;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;
}
