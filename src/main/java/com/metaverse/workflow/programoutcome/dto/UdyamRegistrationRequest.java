package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UdyamRegistrationRequest {

    public String udyamRegistrationDate;
    public String udyamRegistrationNo;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;
}
