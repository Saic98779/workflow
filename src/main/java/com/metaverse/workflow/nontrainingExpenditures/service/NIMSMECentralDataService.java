package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.fileservice.FileUpdateUtil;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.NIMSMECentralData;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.nontrainingExpenditures.Dto.CentralDataRequest;
import com.metaverse.workflow.nontrainingExpenditures.repository.CentralDataRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
@Service@RequiredArgsConstructor
public class NIMSMECentralDataService {
    private final CentralDataRepository centralDataRepository;
    private final NonTrainingSubActivityRepository subActivityRepository;
    private final ProgramSessionFileRepository programSessionFileRepository;
    private final StorageService storageService;

    public WorkflowResponse createCentralData(CentralDataRequest request, MultipartFile file) throws DataException {
        NonTrainingSubActivity subActivity = subActivityRepository.findById(request.getSubActivityId())
                .orElseThrow(() -> new DataException("SubActivity not found", "SUB_ACTIVITY_NOT_FOUND", 400));

        NIMSMECentralData entity = NIMSMECentralDataMapper.mapToCentralDataReq(request, subActivity);
        NIMSMECentralData savedEntity = centralDataRepository.save(entity);

        if (file != null && !file.isEmpty()) {
            String filePath = this.storageFiles(file, savedEntity.getCentralDataId(), "NIMSMECentralData");
            savedEntity.setUploadBillUrl(filePath);
            centralDataRepository.save(savedEntity);

            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .filePath(filePath)
                    .nimsmeCentralData(savedEntity)
                    .build());
        }

        return WorkflowResponse.builder()
                .status(200)
                .message("Central Data saved successfully")
                .data(NIMSMECentralDataMapper.mapToCentralDataRes(savedEntity))
                .build();
    }


    @Transactional
    public WorkflowResponse updateCentralData(Long centralDataId, CentralDataRequest request, MultipartFile file) throws DataException {
        NIMSMECentralData entity = centralDataRepository.findById(centralDataId)
                .orElseThrow(() -> new DataException("Central Data not found", "CENTRAL_DATA_NOT_FOUND", 400));


        NIMSMECentralData updatedEntity = NIMSMECentralDataMapper.mapToCentralDataReq(request, entity.getNonTrainingSubActivity());
        updatedEntity.setCentralDataId(centralDataId);

        String newPath = FileUpdateUtil.replaceFile(
                file,
                updatedEntity.getUploadBillUrl(),
                (uploadedFile) -> this.storageFiles(file, updatedEntity.getCentralDataId(), "NonTrainingExpenditure"),
                () -> centralDataRepository.save(updatedEntity)
        );
        updatedEntity.setUploadBillUrl(newPath);
        programSessionFileRepository.updateFilePathByCentralDataId(
                newPath,
                updatedEntity.getCentralDataId());
        NIMSMECentralData saved = centralDataRepository.save(updatedEntity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Central Data updated successfully")
                .data(NIMSMECentralDataMapper.mapToCentralDataRes(saved))
                .build();
    }


    public WorkflowResponse getCentralDataBySubActivityId(Long subActivityId) throws DataException {
        List<NIMSMECentralData> dataList = centralDataRepository
                .findByNonTrainingSubActivity_SubActivityId(subActivityId)
                .orElseThrow(() -> new DataException("SubActivity not found", "SUB_ACTIVITY_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("Central Data fetched successfully")
                .data(dataList.stream()
                        .map(NIMSMECentralDataMapper::mapToCentralDataRes)
                        .collect(Collectors.toList()))
                .build();
    }


    @Transactional
    public WorkflowResponse deleteCentralData(Long centralDataId) throws DataException {
        NIMSMECentralData entity = centralDataRepository.findById(centralDataId)
                .orElseThrow(() -> new DataException("Central Data not found", "CENTRAL_DATA_NOT_FOUND", 400));

        programSessionFileRepository.deleteByNimsmeCentralData_CentralDataId(centralDataId);
        centralDataRepository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Central Data deleted successfully")
                .build();
    }


    public WorkflowResponse getCentralDataById(Long centralDataId) throws DataException {
        NIMSMECentralData entity = centralDataRepository.findById(centralDataId)
                .orElseThrow(() -> new DataException("Central Data not found", "CENTRAL_DATA_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("Central Data fetched successfully")
                .data(NIMSMECentralDataMapper.mapToCentralDataRes(entity))
                .build();
    }

    public String storageFiles(MultipartFile file, Long centralDataId, String folderName) {
        return storageService.store(file, centralDataId, folderName);
    }
}
