package com.metaverse.workflow.programoutcome.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CopyRightsRequest {
    public String dateOfApplicationFiled;
    public String typeOfIntellectualWorkRegistered;
    public String registrationCertificateReceivedDate;
    public String registrationCertificateNumber;
    public Integer numberOfProductsProtected;
    public String nameOfProductProtected;
    public Double revenueFromCopyrightedMaterial;
    public Double marketValueAfterCopyright;
    public Boolean isInfluenced;
    private Long agencyId;
    private Long participantId;
    private Long organizationId;
}
