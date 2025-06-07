package com.metaverse.workflow.participant.service;

import com.metaverse.workflow.model.Sector;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Builder
@Getter
public class ParticipantResponseForESDPTraining {


    private Long organizationId;
    private String organizationName;
    private String organizationCategory;
    private String organizationType;
    private String nameOfTheSHG;
    private String startupCertificateNo;
    private String natureOfStartup;
    private String areasOfWorking;

    private List<String> programDates;
    private List<Sector> sectors;
    private String memberId;
    private Long participantId;
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
    private String certificateIssueDate;
    private String needAssessmentMethodology;
    private List<Long> programIds;
}
