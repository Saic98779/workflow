package com.metaverse.workflow.programoutcome.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.outcomes.ZEDCertification;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.programoutcome.dto.ZEDCertificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ZEDCertificationMapper {

    private final AgencyRepository agencyRepository;
    private final ParticipantRepository participantRepository;
    private final OrganizationRepository organizationRepository;

    public ZEDCertification toEntity(ZEDCertificationRequest request) throws DataException {
        Agency agency = agencyRepository.findById(request.getAgencyId() == null ? 0 : request.getAgencyId())
                .orElseThrow(() -> new DataException("Agency data not found", "AGENCY-DATA-NOT-FOUND", 400));

        Participant participant = participantRepository.findById(request.getParticipantId() == null ? 0 : request.getParticipantId())
                .orElseThrow(() -> new DataException("Participant data not found", "PARTICIPANT-DATA-NOT-FOUND", 400));

        Organization organization = organizationRepository.findById(request.getOrganizationId() == null ? 0 : request.getOrganizationId())
                .orElseThrow(() -> new DataException("Organization data not found", "ORGANIZATION-DATA-NOT-FOUND", 400));

        return ZEDCertification.builder()
                .machineryType(request.getMachineryType())
                .dprSubmissionDate(DateUtil.stringToDate(request.getDprSubmissionDate(), "dd-MM-yyyy"))
                .amountReleasedDate(DateUtil.stringToDate(request.getAmountReleasedDate(), "dd-MM-yyyy"))
                .releasedValue(request.getReleasedValue())
                .groundingDate(DateUtil.stringToDate(request.getGroundingDate(), "dd-MM-yyyy"))
                .certificationDate(DateUtil.stringToDate(request.getCertificationDate(), "dd-MM-yyyy"))
                .zedCertificationType(request.getZedCertificationType())
                .turnover(request.getTurnover())
                .energyConsumptionKwhHr(request.getEnergyConsumptionKwhHr())
                .productionMtHr(request.getProductionMtHr())
                .defectRatePer100Units(request.getDefectRatePer100Units())
                .isInfluenced(request.getIsInfluenced())
                .agency(agency)
                .participant(participant)
                .organization(organization)
                .build();
    }
}

