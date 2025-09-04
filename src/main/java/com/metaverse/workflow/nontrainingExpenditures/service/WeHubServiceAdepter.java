package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.WeHubSelectedCompanies;
import com.metaverse.workflow.nontrainingExpenditures.Dto.WeHubSelectedCompaniesRequest;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.WeHubSelectedCompaniesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class WeHubServiceAdepter implements WeHubService {

    private final WeHubSelectedCompaniesRepository weHubSelectedCompaniesRepository;
    private final NonTrainingSubActivityRepository subActivityRepository;

    @Override
    public WorkflowResponse create(WeHubSelectedCompaniesRequest request) throws DataException {
        NonTrainingSubActivity subActivity = subActivityRepository.findById(request.getSubActivityId())
                .orElseThrow(() ->new DataException("SubActivity not found","SUB_ACTIVITY_NOT_FOUND",400));
        WeHubSelectedCompanies entity = WeHubMapper.toEntity(request, subActivity);
        WeHubSelectedCompanies savedEntity =   weHubSelectedCompaniesRepository.save(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("Selected Companies saved Successfully")
                .data(WeHubMapper.toResponse(savedEntity))
                .build();
    }

    @Override
    @Transactional
    public WorkflowResponse update(Long candidateId, WeHubSelectedCompaniesRequest request) throws DataException {
        WeHubSelectedCompanies entity = weHubSelectedCompaniesRepository.findById(candidateId)
                .orElseThrow(() ->new DataException("Selected Company not found","SELECTED_COMPANY_NOT_FOUND",400));

        entity.setUdhyamDpiitRegistrationNo(request.getUdhyamDpiitRegistrationNo());
        entity.setApplicationReceivedDate(DateUtil.stringToDate(request.getApplicationReceivedDate(), "dd-MM-yyyy"));
        entity.setApplicationSource(request.getApplicationSource());
        entity.setShortlistingDate(DateUtil.stringToDate(request.getShortlistingDate(), "dd-MM-yyyy"));
        entity.setNeedAssessmentDate(DateUtil.stringToDate(request.getNeedAssessmentDate(), "dd-MM-yyyy"));
        entity.setCandidateFinalised(request.getCandidateFinalised());
        entity.setCohortName(request.getCohortName());
        entity.setBaselineAssessmentDate(DateUtil.stringToDate(request.getBaselineAssessmentDate(), "dd-MM-yyyy"));

        WeHubSelectedCompanies updatedEntity =  weHubSelectedCompaniesRepository.save(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("Selected Companies Updated Successfully")
                .data(WeHubMapper.toResponse(updatedEntity))
                .build();
    }



    @Override
    public WorkflowResponse getBySubActivityId(Long subActivityId) throws DataException {
        List<WeHubSelectedCompanies> companies = weHubSelectedCompaniesRepository
                .findByNonTrainingSubActivity_SubActivityId(subActivityId)
                .orElseThrow(() -> new DataException("SubActivity not found", "SUB_ACTIVITY_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("Selected Companies fetched successfully")
                .data(companies.stream()
                        .map(WeHubMapper::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }


    @Override
    public WorkflowResponse delete(Long candidateId) throws DataException {
        WeHubSelectedCompanies entity = weHubSelectedCompaniesRepository.findById(candidateId)
                .orElseThrow(() -> new DataException("Selected Company not found", "SELECTED_COMPANY_NOT_FOUND", 400));

        weHubSelectedCompaniesRepository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Selected Company deleted successfully")
                .build();
    }

    @Override
    public WorkflowResponse getById(Long candidateId) throws DataException {
        WeHubSelectedCompanies entity = weHubSelectedCompaniesRepository.findById(candidateId)
                .orElseThrow(() -> new DataException("Selected Company not found", "SELECTED_COMPANY_NOT_FOUND", 400));
        return WorkflowResponse.builder()
                .status(200)
                .message("Selected Company fetched successfully")
                .data(WeHubMapper.toResponse(entity))
                .build();
    }

}
