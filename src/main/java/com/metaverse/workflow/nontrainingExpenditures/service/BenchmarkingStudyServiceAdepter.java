package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.BenchmarkingStudy;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.nontrainingExpenditures.Dto.BenchmarkingStudyRequest;
import com.metaverse.workflow.nontrainingExpenditures.repository.BenchmarkingStudyRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BenchmarkingStudyServiceAdepter implements BenchmarkingStudyService {
    private final BenchmarkingStudyRepository benchmarkingStudyRepository;
    private final NonTrainingSubActivityRepository subActivityRepository;
    private final ProgramSessionFileRepository programSessionFileRepository;
    private final StorageService storageService;

    @Override
    public WorkflowResponse createBenchmarkingStudy(BenchmarkingStudyRequest request, MultipartFile file) throws DataException {
        NonTrainingSubActivity subActivity = subActivityRepository.findById(request.getNonTrainingSubActivityId())
                .orElseThrow(() -> new DataException("SubActivity not found", "SUB_ACTIVITY_NOT_FOUND", 400));
        BenchmarkingStudy entity = BenchmarkingStudyMapper.mapToThrustSectorVisitReq(request, subActivity);
        BenchmarkingStudy savedEntity = benchmarkingStudyRepository.save(entity);
        if (file != null && !file.isEmpty()) {
            String filePath = this.storageFiles(file, savedEntity.getBenchmarkingStudyId(), "BenchmarkingStudy");
            savedEntity.setUploadBillUrl(filePath);
            benchmarkingStudyRepository.save(savedEntity);
            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .filePath(filePath)
                    .benchmarkingStudy(savedEntity)
                    .build());
        }
        return WorkflowResponse.builder()
                .status(200)
                .message("Benchmarking Study saved successfully")
                .data(BenchmarkingStudyMapper.mapToThrustSectorVisitRes(savedEntity))
                .build();
    }

    @Override
    @Transactional
    public WorkflowResponse updateBenchmarkingStudy(Long benchmarkingStudyId, BenchmarkingStudyRequest request) throws DataException {
        BenchmarkingStudy entity = benchmarkingStudyRepository.findById(benchmarkingStudyId)
                .orElseThrow(() -> new DataException("Benchmarking Study not found", "BENCHMARKING_STUDY_NOT_FOUND", 400));

        BenchmarkingStudy benchmarkingStudy = BenchmarkingStudyMapper.mapToUpdateBenchmarkingStudy(entity, request);
        BenchmarkingStudy updatedEntity = benchmarkingStudyRepository.save(benchmarkingStudy);

        return WorkflowResponse.builder()
                .status(200)
                .message("Benchmarking Study updated successfully")
                .data(BenchmarkingStudyMapper.mapToThrustSectorVisitRes(updatedEntity))
                .build();
    }

    @Override
    public WorkflowResponse getBenchmarkingStudyBySubActivityId(Long subActivityId) throws DataException {
        List<BenchmarkingStudy> studies = benchmarkingStudyRepository
                .findByNonTrainingSubActivity_SubActivityId(subActivityId)
                .orElseThrow(() -> new DataException("SubActivity not found", "SUB_ACTIVITY_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("Benchmarking Studies fetched successfully")
                .data(studies.stream()
                        .map(BenchmarkingStudyMapper::mapToThrustSectorVisitRes)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    @Override
    public WorkflowResponse deleteBenchmarkingStudy(Long benchmarkingStudyId) throws DataException {
        BenchmarkingStudy entity = benchmarkingStudyRepository.findById(benchmarkingStudyId)
                .orElseThrow(() -> new DataException("Benchmarking Study not found", "BENCHMARKING_STUDY_NOT_FOUND", 400));
        programSessionFileRepository.deleteByBenchmarkingStudy_BenchmarkingStudyId(benchmarkingStudyId);
        benchmarkingStudyRepository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Benchmarking Study deleted successfully")
                .build();
    }

    @Override
    public WorkflowResponse getBenchmarkingStudyById(Long benchmarkingStudyId) throws DataException {
        BenchmarkingStudy entity = benchmarkingStudyRepository.findById(benchmarkingStudyId)
                .orElseThrow(() -> new DataException("Benchmarking Study not found", "BENCHMARKING_STUDY_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("Benchmarking Study fetched successfully")
                .data(BenchmarkingStudyMapper.mapToThrustSectorVisitRes(entity))
                .build();
    }

    public String storageFiles(MultipartFile file, Long TravelAndTransportId, String folderName) {
        String filePath = storageService.store(file, TravelAndTransportId, folderName);
        return filePath;
    }
}
