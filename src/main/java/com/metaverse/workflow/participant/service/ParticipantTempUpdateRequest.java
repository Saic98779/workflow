package com.metaverse.workflow.participant.service;

import lombok.Data;

import java.util.Date;

@Data
public class ParticipantTempUpdateRequest {
    private String participantName;
    private Character gender;
    private String category;
    private Character disability;
    private Long aadharNo;
    private Long mobileNo;
    private String email;
    private String designation;
    private Character isParticipatedBefore;
    private String previousParticipationDetails;
    private Character preTrainingAssessmentConducted;
    private Character postTrainingAssessmentConducted;
    private Character isCertificateIssued;
    private Date certificateIssueDate;
    private String needAssessmentMethodology;
    private Boolean hasError;
    private String errorMessage;
    private Boolean isDeleted;
}
