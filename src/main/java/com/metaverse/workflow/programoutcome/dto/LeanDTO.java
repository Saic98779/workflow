package com.metaverse.workflow.programoutcome.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeanDTO {
    private Long id;
    private String certificationType;
    private Date dateOfCertification;
    private Boolean isLeanConsultantAppointed;
    private Date dateOfAppointed;
    private Double rawMaterialWastage;
    private String rawMaterialWastageUnits;
    private Double productionOutput;
    private String productionOutputUnits;
    private Double powerUsage;
    private String powerUsageUnits;
    private Boolean isInfluenced;

    private String agencyName;
    private String participantName;
    private String organizationName;
    private String influencedParticipantName;

    private Date createdOn;
    private Date updatedOn;
}

