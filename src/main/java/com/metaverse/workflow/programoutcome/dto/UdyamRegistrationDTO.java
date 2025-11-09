package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UdyamRegistrationDTO {
    private Long id;
    private String udyamRegistrationNo;
    private Date udyamRegistrationDate;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;

    private Date createdOn;
    private Date updatedOn;
}

