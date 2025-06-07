package com.metaverse.workflow.participant.service;

import com.metaverse.workflow.common.util.CommonUtil;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.Sector;

import java.util.stream.Collectors;

import static com.metaverse.workflow.common.util.DateUtil.dateToString;
public class ParticipantResponseMapper {
	
	public static ParticipantResponse map(Participant participant)
	{
		return ParticipantResponse.builder()
				.participantId(participant.getParticipantId())
				.participantName(participant.getParticipantName())
				.gender(participant.getGender())
				.category(participant.getCategory())
				.disability(participant.getDisability())
				.aadharNo(participant.getAadharNo())
				.mobileNo(participant.getMobileNo())
				.email(participant.getEmail())
				.designation(participant.getDesignation())
				.isParticipatedBefore(participant.getIsParticipatedBefore())
				.previousParticipationDetails(participant.getPreviousParticipationDetails())
				.preTrainingAssessmentConducted(participant.getPreTrainingAssessmentConducted())
				.postTrainingAssessmentConducted(participant.getPostTrainingAssessmentConducted())
				.isCertificateIssued(participant.getIsCertificateIssued())
				.certificateIssueDate(DateUtil.dateToString(participant.getCertificateIssueDate(), "dd-MM-yyyy"))
				.needAssessmentMethodology(participant.getNeedAssessmentMethodology())
				.organizationId(participant.getOrganization() != null ? participant.getOrganization().getOrganizationId() : null)
				.programIds(participant.getPrograms() != null ? participant.getPrograms().stream().map(program -> program.getProgramId()).collect(Collectors.toList()) : null)
				.organizationName(participant.getOrganization() != null ? participant.getOrganization().getOrganizationName() : null)
				.district(participant.getOrganization() != null ? CommonUtil.districtMap.get( Integer.valueOf(participant.getOrganization().getDistId())) : null)
				.mandal(participant.getOrganization() != null ? CommonUtil.districtMap.get( Integer.valueOf(participant.getOrganization().getMandal())) : null)
				.nameOfVO(participant.getOrganization() != null ? participant.getOrganization().getNameOfTheVO() : null)
				.sectorList(participant.getOrganization() != null ? participant.getOrganization().getSectors().stream().map(Sector::getSectorName).toList() : null)
				.build();
	}

	public static ParticipantResponseForESDPTraining mapForESDPTraining(Participant participant)
	{
		return ParticipantResponseForESDPTraining.builder()
				.organizationId( participant.getOrganization() != null ? participant.getOrganization().getOrganizationId() : null)
				.organizationName(participant.getOrganization() != null ? participant.getOrganization().getOrganizationName(): null)
				.organizationCategory(participant.getOrganization() != null ? participant.getOrganization().getOrganizationCategory(): null)
				.organizationType(participant.getOrganization() != null ? participant.getOrganization().getOrganizationType(): null)
				.nameOfTheSHG(participant.getOrganization() != null ? participant.getOrganization().getNameOfTheSHG(): null)
				.startupCertificateNo(participant.getOrganization() != null ? participant.getOrganization().getStartupCertificateNo() : null)
				.natureOfStartup(participant.getOrganization() != null ? participant.getOrganization().getNatureOfStartup() : null)
				.areasOfWorking(participant.getOrganization() != null ? participant.getOrganization().getAreasOfWorking() : null)
				.programDates(participant.getPrograms().stream().map(program -> dateToString(program.getStartDate(), "dd-MM-yyyy"))
						            .collect(Collectors.toList()))
				.sectors(participant.getOrganization() != null ? participant.getOrganization().getSectors(): null)
				.memberId(participant.getMemberId())
				.participantId(participant.getParticipantId())
				.participantName(participant.getParticipantName())
				.gender(participant.getGender())
				.category(participant.getCategory())
				.disability(participant.getDisability())
				.aadharNo(participant.getAadharNo())
				.mobileNo(participant.getMobileNo())
				.email(participant.getEmail())
				.designation(participant.getDesignation())
				.isParticipatedBefore(participant.getIsParticipatedBefore())
				.previousParticipationDetails(participant.getPreviousParticipationDetails())
				.preTrainingAssessmentConducted(participant.getPreTrainingAssessmentConducted())
				.postTrainingAssessmentConducted(participant.getPostTrainingAssessmentConducted())
				.isCertificateIssued((participant.getIsCertificateIssued()))
				.certificateIssueDate(DateUtil.dateToString(participant.getCertificateIssueDate(),"dd-MM-yyyy"))
				.needAssessmentMethodology(participant.getNeedAssessmentMethodology())
				.programIds(participant.getPrograms().stream().map(Program::getProgramId).collect(Collectors.toList()))
				.build();
	}

	public static ParticipantResponse mapAsprient(Participant participant)
	{
		return ParticipantResponse.builder()
				.participantId(participant.getParticipantId())
				.participantName(participant.getParticipantName())
				.gender(participant.getGender())
				.category(participant.getCategory())
				.disability(participant.getDisability())
				.aadharNo(participant.getAadharNo())
				.mobileNo(participant.getMobileNo())
				.email(participant.getEmail())
				.designation(participant.getDesignation())
				.isParticipatedBefore(participant.getIsParticipatedBefore())
				.previousParticipationDetails(participant.getPreviousParticipationDetails())
				.preTrainingAssessmentConducted(participant.getPreTrainingAssessmentConducted())
				.postTrainingAssessmentConducted(participant.getPostTrainingAssessmentConducted())
				.isCertificateIssued(participant.getIsCertificateIssued())
				.certificateIssueDate(DateUtil.dateToString(participant.getCertificateIssueDate(), "dd-MM-yyyy"))
				.needAssessmentMethodology(participant.getNeedAssessmentMethodology())
				.build();
	}

}
