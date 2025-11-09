package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CopyRightsDTO {
    private Long id;
    private Date dateOfApplicationFiled;
    private String typeOfIntellectualWorkRegistered;
    private Date registrationCertificateReceivedDate;
    private String registrationCertificateNumber;
    private Integer numberOfProductsProtected;
    private String nameOfProductProtected;
    private Double revenueFromCopyrightedMaterial;
    private Double marketValueAfterCopyright;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

