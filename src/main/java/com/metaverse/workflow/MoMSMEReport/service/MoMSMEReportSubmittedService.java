package com.metaverse.workflow.MoMSMEReport.service;


import com.metaverse.workflow.MoMSMEReport.repository.MoMSMEReportSubmittedRepo;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.MoMSMEReportSubmitted;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoMSMEReportSubmittedService {
    private final MoMSMEReportSubmittedRepo submittedRepository;

    public WorkflowResponse saveReport(MoMSMEReportSubmittedDTO dto) throws DataException {
        MoMSMEReportSubmitted entity = MoMSMEReportSubmittedMapper.toEntity(dto);
        MoMSMEReportSubmitted saved = submittedRepository.save(entity);

        return WorkflowResponse.builder().data(MoMSMEReportSubmittedMapper.toDTO(saved))
                .message("Success").status(200)
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {
        MoMSMEReportSubmittedDTO dto = submittedRepository.findById(id)
                .map(MoMSMEReportSubmittedMapper::toDTO)
                .orElseThrow(() -> new DataException("MoMSMEReport not found with id " + id,"MO-MSME_REPORT_NOT_FOUND ",400));

        return WorkflowResponse.builder()
                .status(200)
                .message("Submission retrieved successfully.")
                .data(dto)
                .build();
    }
}
