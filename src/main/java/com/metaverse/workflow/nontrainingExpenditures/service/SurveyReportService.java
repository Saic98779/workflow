package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.fileservice.FileUpdateUtil;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.model.aleap_handholding.SurveyReport;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.Dto.SurveyReportDTO;
import com.metaverse.workflow.nontrainingExpenditures.Dto.SurveyReportMapper;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.SurveyReportRepository;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyReportService {

    private final SurveyReportRepository surveyReportRepository;
    private final NonTrainingActivityRepository nonTrainingActivityRepository;
    private final NonTrainingSubActivityRepository nonTrainingSubActivityRepository;
    private final StorageService storageService;
    private final ProgramSessionFileRepository programSessionFileRepository;

    public WorkflowResponse create(SurveyReportDTO dto, MultipartFile file) throws DataException {

        NonTrainingActivity activity = nonTrainingActivityRepository.findById(dto.getNonTrainingActivityId())
                .orElseThrow(() ->
                        new DataException("Activity not found", "ACTIVITY_NOT_FOUND", 400));

        NonTrainingSubActivity subActivity = nonTrainingSubActivityRepository.findById(dto.getNonTrainingSubActivityId())
                .orElseThrow(() ->
                        new DataException("Sub Activity not found", "SUB_ACTIVITY_NOT_FOUND", 400));

        SurveyReport surveyReport = SurveyReportMapper.toEntity(dto, activity, subActivity);

        SurveyReport saved = surveyReportRepository.save(surveyReport);

        if (file != null && !file.isEmpty()) {

            String filePath = storageFiles(file, saved.getId(), "SurveyReport");

            saved.setReportFile(filePath);

            surveyReportRepository.save(saved);

            programSessionFileRepository.save(
                    ProgramSessionFile.builder()
                            .fileType("Survey Report")
                            .filePath(filePath)
                            .surveyReport(saved)
                            .build()
            );
        }

        return WorkflowResponse.builder()
                .status(200)
                .message("Survey Report saved successfully")
                .data(SurveyReportMapper.toDTO(saved))
                .build();
    }

    public SurveyReportDTO update(Long id, SurveyReportDTO dto, MultipartFile file) throws DataException {

        SurveyReport existing = surveyReportRepository.findById(id)
                .orElseThrow(() ->
                        new DataException("Survey Report not found", "SURVEY_REPORT_NOT_FOUND", 400));

        // Update only editable fields
        existing.setReportSubmissionDate(DateUtil.covertStringToDate(dto.getReportSubmissionDate()));
        existing.setApprovalDate(DateUtil.covertStringToDate(dto.getApprovalDate()));

        String newPath = FileUpdateUtil.replaceFile(
                file,
                existing.getReportFile(),
                uploadedFile -> storageFiles(uploadedFile, existing.getId(), "SurveyReport"),
                () -> surveyReportRepository.save(existing)
        );

        existing.setReportFile(newPath);

        SurveyReport saved = surveyReportRepository.save(existing);

        // Update file path if ProgramSessionFile exists
        programSessionFileRepository.findBySurveyReportId(saved.getId())
                .ifPresent(programSessionFile -> {
                    programSessionFile.setFilePath(newPath);
                    programSessionFileRepository.save(programSessionFile);
                });

        return SurveyReportMapper.toDTO(saved);
    }

    public List<SurveyReportDTO> getAll() {
        return surveyReportRepository.findAll()
                .stream()
                .map(SurveyReportMapper::toDTO)
                .toList();
    }

    public SurveyReportDTO getById(Long id) throws DataException {
        return surveyReportRepository.findById(id)
                .map(SurveyReportMapper::toDTO)
                .orElseThrow(() -> new DataException(
                        "Survey Report not found",
                        "SURVEY_REPORT_NOT_FOUND",
                        400
                ));
    }

    public WorkflowResponse getBySubActivityId(Long subActivityId) {

        List<SurveyReport> reports = surveyReportRepository
                .findByNonTrainingSubActivity_subActivityId(subActivityId);

        return WorkflowResponse.builder()
                .status(200)
                .message("success")
                .data(reports.stream()
                        .map(SurveyReportMapper::toDTO)
                        .toList())
                .build();
    }
    @Transactional
    public WorkflowResponse delete(Long id) throws DataException {

        SurveyReport surveyReport = surveyReportRepository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Survey Report not found",
                        "SURVEY_REPORT_NOT_FOUND",
                        400));

        programSessionFileRepository.deleteBySurveyReport_Id(id);

        surveyReportRepository.delete(surveyReport);

        return WorkflowResponse.builder()
                .status(200)
                .message("Survey Report deleted successfully")
                .build();
    }
    public String storageFiles(MultipartFile file, Long id, String folderName) {
        return storageService.store(file, id, folderName);
    }
}
