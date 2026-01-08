package com.metaverse.workflow.tgtpc_handholding.service;

import com.metaverse.workflow.common.fileservice.FileUpdateUtil;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.model.tgtpc_handholding.TestingQualityCertificationSupport;
import com.metaverse.workflow.model.tgtpc_handholding.TGTPCHandholdingSupport;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import com.metaverse.workflow.tgtpc_handholding.repository.TestingQualityCertificationSupportRepository;
import com.metaverse.workflow.tgtpc_handholding.request_dto.TestingQualityCertificationSupportRequest;
import com.metaverse.workflow.tgtpc_handholding.response_dto.TestingQualityCertificationSupportResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestingQualityCertificationSupportService {

    private final TGTPCHandholdingMasterService tgtpcHandholdingMasterService;
    private final TestingQualityCertificationSupportRepository repo;
    private final StorageService storageService;
    private final ProgramSessionFileRepository programSessionFileRepository;

    public String storageFiles(MultipartFile file, Long TravelAndTransportId, String folderName) {
        String filePath = storageService.store(file, TravelAndTransportId, folderName);
        return filePath;
    }
    @Transactional
    public WorkflowResponse save(TestingQualityCertificationSupportRequest request, MultipartFile file) throws DataException {

        TGTPCHandholdingSupport support = tgtpcHandholdingMasterService.save(request.getTgtpcHandholdingSupportRequest());

        TestingQualityCertificationSupport entity = HandholdingRequestMapper.mapToTestingQualityCertificationSupport(request, support);
        entity = repo.save(entity);
        if (file != null && !file.isEmpty()) {
            String filePath = this.storageFiles(file, entity.getTestingId(), "TestingQualityCertificationSupport");
            entity.setTestResultFilePath(filePath);
            repo.save(entity);
            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .filePath(filePath)
                    .testingQualityCertificationSupport(entity)
                    .build());
        }

        return WorkflowResponse.builder()
                .status(200)
                .message("Testing Quality Certification Support saved successfully")
                .data(HandholdingResponseMapper.mapToTestingQualityCertificationSupportRes(entity))
                .build();
    }

    @Transactional
    public WorkflowResponse update(Long id, TestingQualityCertificationSupportRequest request, MultipartFile file) throws DataException {

        TestingQualityCertificationSupport existing = repo.findById(id).orElseThrow(() -> new DataException(
                "Testing Quality Certification Support not found with id " + id,
                "TESTING_SUPPORT_NOT_FOUND",
                404
        ));

        TGTPCHandholdingSupport support = tgtpcHandholdingMasterService.update(
                existing.getTgtpcHandholdingSupport().getTgtpcHandholdingSupportId(),
                request.getTgtpcHandholdingSupportRequest()
        );

        existing.setTgtpcHandholdingSupport(support);
        existing.setTestingAgencyName(request.getTestingAgencyName() != null ? request.getTestingAgencyName() : existing.getTestingAgencyName());
        existing.setDateOfTest(request.getDateOfTest() != null ? DateUtil.covertStringToDate(request.getDateOfTest()) : existing.getDateOfTest());
        existing.setProductTested(request.getProductTested() != null ? request.getProductTested() : existing.getProductTested());
        existing.setTestResultsDate(request.getTestResultsDate() != null ? DateUtil.covertStringToDate(request.getTestResultsDate()) : existing.getTestResultsDate());
        existing.setCertificationOrTestFindings(request.getCertificationOrTestFindings() != null ? request.getCertificationOrTestFindings() : existing.getCertificationOrTestFindings());

        TestingQualityCertificationSupport updated = repo.save(existing);

        String newPath = FileUpdateUtil.replaceFile(
                file,
                updated.getTestResultFilePath(),
                (uploadedFile) -> this.storageFiles(file, updated.getTestingId(), "TestingQualityCertificationSupport"),
                () -> repo.save(updated)
        );
        updated.setTestResultFilePath(newPath);
        programSessionFileRepository.updateFilePathByNonTrainingExpenditureId(
                newPath,
                updated.getTestingId()
        );

        return WorkflowResponse.builder()
                .status(200)
                .message("Testing Quality Certification Support updated successfully")
                .data(HandholdingResponseMapper.mapToTestingQualityCertificationSupportRes(updated))
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {
        TestingQualityCertificationSupport entity = repo.findById(id).orElseThrow(() -> new DataException(
                "Testing Quality Certification Support not found with id " + id,
                "TESTING_SUPPORT_NOT_FOUND",
                404
        ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(HandholdingResponseMapper.mapToTestingQualityCertificationSupportRes(entity))
                .build();
    }

    public WorkflowResponse getBySubActivityId(Long subActivityId) {
        List<TestingQualityCertificationSupportResponse> response = repo
                .findByTgtpcHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(HandholdingResponseMapper::mapToTestingQualityCertificationSupportRes)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    @Transactional
    public WorkflowResponse delete(Long id) throws DataException {
        TestingQualityCertificationSupport entity = repo.findById(id).orElseThrow(() -> new DataException(
                "Testing Quality Certification Support not found with id " + id,
                "TESTING_SUPPORT_NOT_FOUND",
                404
        ));
        repo.delete(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("Testing Quality Certification Support deleted successfully")
                .build();
    }
}
