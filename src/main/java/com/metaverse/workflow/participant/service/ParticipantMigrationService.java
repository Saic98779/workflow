package com.metaverse.workflow.participant.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.participant.repository.ParticipantTempRepository;
import com.metaverse.workflow.program.service.ProgramResponseMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ParticipantMigrationService {

    private final ParticipantTempRepository participantTempRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantTempResponseMapper responseMapper;


    @Transactional
    public Map<String, List<?>> migrateParticipants(List<Long> tempIds) {
        List<Long> successes = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        if (tempIds == null || tempIds.isEmpty()) {
            errors.add("No participant IDs provided for migration.");
            return Map.of("successes", successes, "errors", errors);
        }
        List<Long> existingTempIds = participantTempRepository.findParticipantTempIdsByProgramId(tempIds.get(0));
        for (Long tempId : existingTempIds) {
            participantTempRepository.findById(tempId).ifPresentOrElse(temp -> {
                try {
                    String orgName = temp.getOrganizationTemp() != null
                            ? temp.getOrganizationTemp().getOrganizationName() : null;

                    if (participantRepository.existsByParticipantNameAndOrganization_OrganizationName(
                            temp.getParticipantName(), orgName)) {
                        String errorMsg = String.format("Duplicate: Participant '%s' in organization '%s'",
                                temp.getParticipantName(), orgName != null ? orgName : "N/A");

                        errors.add(errorMsg);
                        setErrorOnTemp(temp, true, errorMsg);
                        return;
                    }

                    Participant participant = mapTempToParticipant(temp);
                    participantRepository.save(participant);

                    // Soft delete + clear errors on success
                    setErrorOnTemp(temp, false, null);
                    temp.setIsDeleted(true);
                    participantTempRepository.save(temp);

                    successes.add(tempId);
                } catch (Exception e) {
                    String errorMsg = String.format("Error migrating tempId %d: %s", tempId, e.getMessage());
                    errors.add(errorMsg);
                    setErrorOnTemp(temp, true, errorMsg);
                }
            }, () -> errors.add("ParticipantTemp not found with id: " + tempId));
        }

        return Map.of("successes", successes, "errors", errors);
    }

    private void setErrorOnTemp(ParticipantTemp temp, boolean hasError, String errorMessage) {
        temp.setHasError(hasError);
        temp.setErrorMessage(errorMessage);
        participantTempRepository.save(temp);
    }

    public WorkflowResponse getTempProgramParticipants(Long id, Long agencyId, Boolean failed) {
        List<ParticipantTemp> participantTempList;

        if (failed != null && failed) {
            // Fetch only records with error and not deleted
            participantTempList = participantTempRepository.findByHasErrorTrueAndIsDeletedFalse();
        } else {
            // Normal logic with isDeleted = false filter added
            if (id == -1) {
                participantTempList = participantTempRepository.findByIsDeletedFalse();
            } else if (agencyId != null) {
                participantTempList = participantTempRepository.findByPrograms_Agency_AgencyIdAndIsDeletedFalse(agencyId);
            } else {
                participantTempList = participantTempRepository.findByPrograms_ProgramIdAndIsDeletedFalse(id);
            }
        }

        List<ParticipantResponse> response = ProgramResponseMapper.mapProgramTempParticipants(participantTempList);
        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }


    private Participant mapTempToParticipant(ParticipantTemp temp) {
        List<Program> programsCopy = new ArrayList<>(temp.getPrograms());

        return Participant.builder()
                .participantName(temp.getParticipantName())
                .gender(temp.getGender())
                .category(temp.getCategory())
                .disability(temp.getDisability())
                .aadharNo(temp.getAadharNo())
                .mobileNo(temp.getMobileNo())
                .email(temp.getEmail())
                .designation(temp.getDesignation())
                .isParticipatedBefore(temp.getIsParticipatedBefore())
                .previousParticipationDetails(temp.getPreviousParticipationDetails())
                .preTrainingAssessmentConducted(temp.getPreTrainingAssessmentConducted())
                .postTrainingAssessmentConducted(temp.getPostTrainingAssessmentConducted())
                .isCertificateIssued(temp.getIsCertificateIssued())
                .certificateIssueDate(temp.getCertificateIssueDate())
                .needAssessmentMethodology(temp.getNeedAssessmentMethodology())
                .organization(temp.getOrganizationTemp() != null ? mapOrganizationTemp(temp.getOrganizationTemp()) : null)
                .programs(programsCopy)
                .build();
    }

    private Organization mapOrganizationTemp(OrganizationTemp orgTemp) {
        if (orgTemp == null) return null;

        Organization org = new Organization();
        org.setOrganizationName(orgTemp.getOrganizationName());
        org.setOrganizationCategory(orgTemp.getOrganizationCategory());
        org.setOrganizationType(orgTemp.getOrganizationType());
        org.setUdyamregistrationNo(orgTemp.getUdyamregistrationNo());
        org.setDateOfRegistration(orgTemp.getDateOfRegistration());
        org.setStartupCertificateNo(orgTemp.getStartupCertificateNo());
        org.setNatureOfStartup(orgTemp.getNatureOfStartup());
        org.setAreasOfWorking(orgTemp.getAreasOfWorking());
        org.setIncorporationDate(orgTemp.getIncorporationDate());
        org.setDateOfIssue(orgTemp.getDateOfIssue());
        org.setValidUpto(orgTemp.getValidUpto());
        org.setStateId(orgTemp.getStateId());
        org.setDistId(orgTemp.getDistId());
        org.setMandal(orgTemp.getMandal());
        org.setTown(orgTemp.getTown());
        org.setStreetNo(orgTemp.getStreetNo());
        org.setHouseNo(orgTemp.getHouseNo());
        org.setLatitude(orgTemp.getLatitude());
        org.setLongitude(orgTemp.getLongitude());
        org.setContactNo(orgTemp.getContactNo());
        org.setEmail(orgTemp.getEmail());
        org.setWebsite(orgTemp.getWebsite());
        org.setOwnerName(orgTemp.getOwnerName());
        org.setOwnerContactNo(orgTemp.getOwnerContactNo());
        org.setOwnerEmail(orgTemp.getOwnerEmail());
        org.setOwnerAddress(orgTemp.getOwnerAddress());
        org.setNameOfTheSHG(orgTemp.getNameOfTheSHG());
        org.setNameOfTheVO(orgTemp.getNameOfTheVO());
        org.setGramaPanchayat(orgTemp.getGramaPanchayat());

        return org;
    }


    public ParticipantTempResponse updateParticipantTemp(Long id, ParticipantTempUpdateRequest req) {
        ParticipantTemp entity = participantTempRepository.findByParticipantTempId(id);

        if (req.getParticipantName() != null) entity.setParticipantName(req.getParticipantName());
        if (req.getGender() != null) entity.setGender(req.getGender());
        if (req.getCategory() != null) entity.setCategory(req.getCategory());
        if (req.getDisability() != null) entity.setDisability(req.getDisability());
        if (req.getAadharNo() != null) entity.setAadharNo(req.getAadharNo());
        if (req.getMobileNo() != null) entity.setMobileNo(req.getMobileNo());
        if (req.getEmail() != null) entity.setEmail(req.getEmail());
        if (req.getDesignation() != null) entity.setDesignation(req.getDesignation());
        if (req.getIsParticipatedBefore() != null) entity.setIsParticipatedBefore(req.getIsParticipatedBefore());
        if (req.getPreviousParticipationDetails() != null) entity.setPreviousParticipationDetails(req.getPreviousParticipationDetails());
        if (req.getPreTrainingAssessmentConducted() != null) entity.setPreTrainingAssessmentConducted(req.getPreTrainingAssessmentConducted());
        if (req.getPostTrainingAssessmentConducted() != null) entity.setPostTrainingAssessmentConducted(req.getPostTrainingAssessmentConducted());
        if (req.getIsCertificateIssued() != null) entity.setIsCertificateIssued(req.getIsCertificateIssued());
        if (req.getCertificateIssueDate() != null) entity.setCertificateIssueDate(req.getCertificateIssueDate());
        if (req.getNeedAssessmentMethodology() != null) entity.setNeedAssessmentMethodology(req.getNeedAssessmentMethodology());
        if (req.getHasError() != null) entity.setHasError(req.getHasError());
        if (req.getErrorMessage() != null) entity.setErrorMessage(req.getErrorMessage());
        if (req.getIsDeleted() != null) entity.setIsDeleted(req.getIsDeleted());

        ParticipantTemp savedEntity = participantTempRepository.save(entity);

        return responseMapper.convertToEntity(savedEntity);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!participantTempRepository.existsById(id)) {
            throw new EntityNotFoundException("ParticipantTemp with id " + id + " not found");
        }
        ParticipantTemp participantTemp = participantTempRepository.findByParticipantTempId(id);
        participantTempRepository.delete(participantTemp);
    }
}

