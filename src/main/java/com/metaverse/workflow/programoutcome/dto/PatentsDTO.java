package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatentsDTO {
    private Long id;
    private String nameOfPatent;
    private String typeOfPatent;
    private String patentNumber;
    private Date patentIssueDate;
    private String patentCoverage;
    private Double annualRevenue;
    private Date dateOfExport;
    private Double valueOfExport;
    private String countryOfExport;
    private Integer totalJobsCreated;
    private String nameOfAward;
    private Date dateOfAward;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

