package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.WeHubSelectedCompanies;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubSelectedCompaniesRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubSelectedCompaniesResponse;

public class WeHubMapper {

    public static WeHubSelectedCompanies toEntity(WeHubSelectedCompaniesRequest dto, NonTrainingSubActivity subActivity) {
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
}
