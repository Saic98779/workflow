package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontrainingExpenditures.Dto.*;

public class WeHubMapper {
    private static final String DATE_PATTERN = "dd-MM-yyyy";
    public static WeHubSelectedCompanies toEntity(WeHubSelectedCompaniesRequest dto, NonTrainingSubActivity subActivity,Organization organization) {
        return WeHubSelectedCompanies.builder()
                .udhyamDpiitRegistrationNo(dto.getUdhyamDpiitRegistrationNo())
                .applicationReceivedDate(DateUtil.stringToDate(dto.getApplicationReceivedDate(), "dd-MM-yyyy"))
                .applicationSource(dto.getApplicationSource())
                .shortlistingDate(DateUtil.stringToDate(dto.getShortlistingDate(), "dd-MM-yyyy"))
                .needAssessmentDate(DateUtil.stringToDate(dto.getNeedAssessmentDate(), "dd-MM-yyyy"))
                .candidateFinalised(dto.getCandidateFinalised())
                .cohortName(dto.getCohortName())
                .baselineAssessmentDate(DateUtil.stringToDate(dto.getBaselineAssessmentDate(), "dd-MM-yyyy"))
                .nonTrainingSubActivity(subActivity)
                .organization(organization)
                .build();
    }

    public static WeHubSelectedCompaniesResponse toResponse(WeHubSelectedCompanies entity) {
        WeHubSelectedCompaniesResponse response = new WeHubSelectedCompaniesResponse();
        response.setCandidateId(entity.getCandidateId());
        response.setUdhyamDpiitRegistrationNo(entity.getUdhyamDpiitRegistrationNo());
        response.setApplicationReceivedDate(DateUtil.dateToString(entity.getApplicationReceivedDate(), "dd-MM-yyyy"));
        response.setApplicationSource(entity.getApplicationSource());
        response.setShortlistingDate(DateUtil.dateToString(entity.getShortlistingDate(), "dd-MM-yyyy"));
        response.setNeedAssessmentDate(DateUtil.dateToString(entity.getNeedAssessmentDate(), "dd-MM-yyyy"));
        response.setCandidateFinalised(entity.getCandidateFinalised());
        response.setCohortName(entity.getCohortName());
        response.setBaselineAssessmentDate(DateUtil.dateToString(entity.getBaselineAssessmentDate(), "dd-MM-yyyy"));
        if (entity.getNonTrainingSubActivity() != null) {
            response.setSubActivityId(entity.getNonTrainingSubActivity().getSubActivityId());
            response.setSubActivityName(entity.getNonTrainingSubActivity().getSubActivityName());
        }
        return response;
    }


    public static WeHubHandholding mapToWehubHandholdingReq(WeHubHandholdingRequest dto, Organization organization, NonTrainingSubActivity subActivity) {
        if (dto == null) return null;

        WeHubHandholding entity = new WeHubHandholding();
        entity.setOrganization(organization);
        entity.setStatusProductDiversification(dto.getStatusProductDiversification());
        entity.setDateSupportReceived(DateUtil.stringToDate(dto.getDateSupportReceived(), DATE_PATTERN));
        entity.setIprName(dto.getIprName());
        entity.setIprFilingDate(DateUtil.stringToDate(dto.getIprFilingDate(), DATE_PATTERN));
        entity.setStatusTrademarkFiling(dto.getStatusTrademarkFiling());
        entity.setTrademarkFilingDate(DateUtil.stringToDate(dto.getTrademarkFilingDate(), DATE_PATTERN));
        entity.setStatusInventoryManagement(dto.getStatusInventoryManagement());
        entity.setInventoryAdoptionDate(DateUtil.stringToDate(dto.getInventoryAdoptionDate(), DATE_PATTERN));
        entity.setLeanName(dto.getLeanName());
        entity.setLeanAdoptionDate(DateUtil.stringToDate(dto.getLeanAdoptionDate(), DATE_PATTERN));
        entity.setNewMachineryAdoption(dto.getNewMachineryAdoption());
        entity.setEstablishmentDate(DateUtil.stringToDate(dto.getEstablishmentDate(), DATE_PATTERN));
        entity.setStatusTechnologyRedesign(dto.getStatusTechnologyRedesign());
        entity.setTechnologyRedesignDate(DateUtil.stringToDate(dto.getTechnologyRedesignDate(), DATE_PATTERN));
        entity.setStatusDigitalSolution(dto.getStatusDigitalSolution());
        entity.setDigitalSolutionDate(DateUtil.stringToDate(dto.getDigitalSolutionDate(), DATE_PATTERN));
        entity.setInnovativeProcessName(dto.getInnovativeProcessName());
        entity.setInnovativeProcessDate(DateUtil.stringToDate(dto.getInnovativeProcessDate(), DATE_PATTERN));
        entity.setSkillTrainingName(dto.getSkillTrainingName());
        entity.setSkillTrainingPerson(dto.getSkillTrainingPerson());
        entity.setSkillTrainingDate(DateUtil.stringToDate(dto.getSkillTrainingDate(), DATE_PATTERN));
        entity.setNonTrainingSubActivity(subActivity);

        return entity;
    }


    public static WeHubHandholdingResponse mapToWehubHandholdingRes(WeHubHandholding entity) {
        if (entity == null) return null;

        WeHubHandholdingResponse dto = new WeHubHandholdingResponse();
        dto.setHandholdingId(entity.getHandholdingId());
        dto.setOrganizationId(entity.getOrganization() != null ? entity.getOrganization().getOrganizationId() : null);
        dto.setOrganizationName(entity.getOrganization() != null ? entity.getOrganization().getOrganizationName() : null);

        dto.setStatusProductDiversification(entity.getStatusProductDiversification());
        dto.setDateSupportReceived(DateUtil.dateToString(entity.getDateSupportReceived(), DATE_PATTERN));
        dto.setIprName(entity.getIprName());
        dto.setIprFilingDate(DateUtil.dateToString(entity.getIprFilingDate(), DATE_PATTERN));
        dto.setStatusTrademarkFiling(entity.getStatusTrademarkFiling());
        dto.setTrademarkFilingDate(DateUtil.dateToString(entity.getTrademarkFilingDate(), DATE_PATTERN));
        dto.setStatusInventoryManagement(entity.getStatusInventoryManagement());
        dto.setInventoryAdoptionDate(DateUtil.dateToString(entity.getInventoryAdoptionDate(), DATE_PATTERN));
        dto.setLeanName(entity.getLeanName());
        dto.setLeanAdoptionDate(DateUtil.dateToString(entity.getLeanAdoptionDate(), DATE_PATTERN));
        dto.setNewMachineryAdoption(entity.getNewMachineryAdoption());
        dto.setEstablishmentDate(DateUtil.dateToString(entity.getEstablishmentDate(), DATE_PATTERN));
        dto.setStatusTechnologyRedesign(entity.getStatusTechnologyRedesign());
        dto.setTechnologyRedesignDate(DateUtil.dateToString(entity.getTechnologyRedesignDate(), DATE_PATTERN));
        dto.setStatusDigitalSolution(entity.getStatusDigitalSolution());
        dto.setDigitalSolutionDate(DateUtil.dateToString(entity.getDigitalSolutionDate(), DATE_PATTERN));
        dto.setInnovativeProcessName(entity.getInnovativeProcessName());
        dto.setInnovativeProcessDate(DateUtil.dateToString(entity.getInnovativeProcessDate(), DATE_PATTERN));
        dto.setSkillTrainingName(entity.getSkillTrainingName());
        dto.setSkillTrainingPerson(entity.getSkillTrainingPerson());
        dto.setSkillTrainingDate(DateUtil.dateToString(entity.getSkillTrainingDate(), DATE_PATTERN));
        dto.setSubActivityId(entity.getNonTrainingSubActivity() != null ? entity.getNonTrainingSubActivity().getSubActivityId() : null);
        dto.setSubActivityName(entity.getNonTrainingSubActivity() != null ? entity.getNonTrainingSubActivity().getSubActivityName() : null);

        return dto;
    }

    public static WeHubSDG mapToEeHubSDGReq(WeHubSDGRequest dto, Organization organization, NonTrainingSubActivity nonTrainingSubActivity) {
        WeHubSDG entity = new WeHubSDG();
        entity.setOrganization(organization);
        entity.setNonTrainingSubActivity(nonTrainingSubActivity);
        entity.setAdoptionStatus(dto.getAdoptionStatus());
        entity.setTechnologyAdopted(dto.getTechnologyAdopted());
        entity.setEnvCompCert(dto.getEnvCompCert());
        entity.setDateOfCert(DateUtil.stringToDate(dto.getDateOfCert(),DATE_PATTERN));
        return entity;
    }

    // Entity -> Response DTO
    public static WeHubSDGResponse mapToEeHubSDGRes(WeHubSDG entity) {
        WeHubSDGResponse dto = new WeHubSDGResponse();
        dto.setEeHubSDGId(entity.getEeHubSDGId());
        dto.setOrganizationId(entity.getOrganization() != null ? entity.getOrganization().getOrganizationId() : null);
        dto.setOrganizationName(entity.getOrganization() != null ? entity.getOrganization().getOrganizationName() : null);
        dto.setNonTrainingSubActivityId(entity.getNonTrainingSubActivity() != null ? entity.getNonTrainingSubActivity().getSubActivityId() : null);
        dto.setNonTrainingSubActivityName(entity.getNonTrainingSubActivity() != null ? entity.getNonTrainingSubActivity().getSubActivityName() : null);
        dto.setAdoptionStatus(entity.getAdoptionStatus());
        dto.setTechnologyAdopted(entity.getTechnologyAdopted());
        dto.setEnvCompCert(entity.getEnvCompCert());
        dto.setDateOfCert(DateUtil.dateToString(entity.getDateOfCert(),DATE_PATTERN));

        return dto;
    }
}
