package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ONDCRegistrationRequest {
    public String ondcRegistrationNo;
    public String ondcRegistrationDate;
    private Long participantId;
    private Long organizationId;
    private Long agencyId;

}
