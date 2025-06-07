package com.metaverse.workflow.programoutcome.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class NSICRequest {
    public String govtAgencyProcured;
    public String dateOfProcurement;
    public String typeOfProductSupplied;
    public Double valueOfProcurement;
    public Double costSavingsTender;
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
}
