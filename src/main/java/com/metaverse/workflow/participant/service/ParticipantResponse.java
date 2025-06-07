package com.metaverse.workflow.participant.service;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.metaverse.workflow.model.Sector;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParticipantResponse {
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
	private Long organizationId;
	private List<Long> programIds;
	private String organizationName;
	private String district;
	private String mandal;
	private String nameOfVO;
	private List<String> sectorList;

}
