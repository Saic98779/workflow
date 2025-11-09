package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CGTMSETransactionDTO {
    private Long id;
    private String productName;
    private String purpose;
    private Double valueReleased;
    private Date approvalDate;
    private Integer employmentMale;
    private Integer employmentFemale;

    private String agencyName;
    private String participantName;
    private String organizationName;

    private Date createdOn;
    private Date updatedOn;
}

