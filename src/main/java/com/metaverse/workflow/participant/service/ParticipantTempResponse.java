package com.metaverse.workflow.participant.service;

import lombok.Data;
import java.util.Date;

@Data
public class ParticipantTempResponse {
    private Long id;
    private String participantName;
    private Character gender; // match entity
    private String category;
    private Character disability; // match entity
    private Long aadharNo; // match entity
    private Long mobileNo; // match entity
    private String email;
    private String designation;
    private Character isParticipatedBefore; // match entity
    private String previousParticipationDetails;
    private Character preTrainingAssessmentConducted; // match entity
    private Character postTrainingAssessmentConducted; // match entity
    private Character isCertificateIssued; // match entity
    private Date certificateIssueDate; // match entity
    private String needAssessmentMethodology;
    private Boolean hasError;
    private String errorMessage;
    private Boolean isDeleted;
}
