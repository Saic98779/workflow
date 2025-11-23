package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.fileservice.FileUpdateUtil;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingConsumablesBulkRepo;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingConsumablesTransactionsRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NonTrainingConsumablesBulkService {

    private final NonTrainingConsumablesBulkRepo bulkRepo;
    private final AgencyRepository agencyRepo;
    private final NonTrainingSubActivityRepository subActivityRepo;
    private final ProgramSessionFileRepository programSessionFileRepository;
    private final StorageService storageService;
    private final NonTrainingConsumablesTransactionsRepository transactionsRepository;

    public WorkflowResponse saveBulkConsumable(NonTrainingConsumablesBulkDto dto, MultipartFile file) throws DataException {
            Agency agency = agencyRepo.findById(dto.getAgencyId())
                    .orElseThrow(() -> new DataException("Agency not found", "AGENCY_NOT_FOUND", 400));
            NonTrainingSubActivity subActivity = subActivityRepo.findById(dto.getSubActivityId())
                    .orElseThrow(() -> new DataException("Activity not found", "ACTIVITY_NOT_FOUND", 400));

            NonTrainingConsumablesBulk entity = NonTrainingConsumablesBulkMapper.mapToBulkConsumable(dto);
            entity.setAgency(agency);
            entity.setNonTrainingSubActivity(subActivity);

            NonTrainingConsumablesBulk saved = bulkRepo.save(entity);

            if (file != null && !file.isEmpty()) {
                String url = storageFile(file, saved.getId(), "NonTrainingConsumablesBulk");
                saved.setUploadBillUrl(url);
                bulkRepo.save(saved);

                programSessionFileRepository.save(ProgramSessionFile.builder()
                        .fileType("File")
                        .filePath(url)
                        .nonTrainingConsumablesBulk(saved)
                        .build());
            }

            return WorkflowResponse.builder()
                    .status(200)
                    .message("Saved")
                    .data(NonTrainingConsumablesBulkMapper.mapToBulkConsumableDto(saved))
                    .build();

    }

    @Transactional
    public NonTrainingConsumablesBulkDto updateBulkConsumable(Long id, NonTrainingConsumablesBulkDto dto, MultipartFile file) throws DataException {

        NonTrainingConsumablesBulk existing = bulkRepo.findById(id)
                .orElseThrow(() -> new DataException("Record not found", "BULK_NOT_FOUND", 400));

        NonTrainingConsumablesBulk updated = NonTrainingConsumablesBulkMapper.updateBulkConsumable(existing,dto);

        String newFilePath = FileUpdateUtil.replaceFile(
                file,
                existing.getUploadBillUrl(),
                (uploadedFile) ->
                        this.storageFile(uploadedFile, updated.getId(), "NonTrainingConsumablesBulk"),
                () -> bulkRepo.save(updated)
        );

        updated.setUploadBillUrl(newFilePath);

        programSessionFileRepository.updateFilePathByNonTrainingConsumablesBulkId(
                newFilePath,
                updated.getId()
        );

        return NonTrainingConsumablesBulkMapper.mapToBulkConsumableDto(bulkRepo.save(updated));
    }

    @Transactional
    public void deleteBulkConsumable(Long id) throws DataException {

        NonTrainingConsumablesBulk existing = bulkRepo.findById(id)
                .orElseThrow(() -> new DataException("Record not found", "BULK_NOT_FOUND", 400));

        // 1. Delete children in program_session_file table
        programSessionFileRepository.deleteByNonTrainingConsumablesBulkId(id);

        // 2. Delete children in transactions table
        transactionsRepository.deleteByNonTrainingConsumablesBulkId(id);

        // 3. Delete the parent
        bulkRepo.delete(existing);
    }

    public  NonTrainingConsumablesBulkDto getNonTrainingConsumablesBulk(Long id) throws DataException {
        return NonTrainingConsumablesBulkMapper.mapToBulkConsumableDto(
                bulkRepo.findById(id)
                        .orElseThrow(() -> new DataException("Record not found", "BULK_NOT_FOUND", 400))
        );
    }
    public List<NonTrainingConsumablesBulkDto> getBySubActivity(Long subActivityId) {
        return bulkRepo.findByNonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(NonTrainingConsumablesBulkMapper::mapToBulkConsumableDto)
                .toList();
    }


    // Dummy Storage Method (your existing implementation)
    private String storageFile(MultipartFile file, Long id, String folder) {
        String filePath = storageService.store(file, id, folder);
        return filePath;
    }
}

