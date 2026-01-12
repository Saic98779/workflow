package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.AccessToPackagingLabellingAndBrandingRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.AccessToPackagingLabellingAndBrandingRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.AccessToPackagingLabellingAndBrandingResponse;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.model.aleap_handholding.AccessToPackagingLabellingAndBranding;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessToPackagingLabellingAndBrandingService {

    private final StorageService storageService;
    private final HandholdingSupportService service;
    private final AccessToPackagingLabellingAndBrandingRepository repository;
    private final ProgramSessionFileRepository programSessionFileRepository;
    private final OrganizationRepository organizationRepository;

    public String storageFiles(MultipartFile file, Long recordId, String folderName) {
        return storageService.store(file, recordId, folderName);
    }

    @Transactional
    public WorkflowResponse save(
            AccessToPackagingLabellingAndBrandingRequest request,
            MultipartFile image1,
            MultipartFile image2,
            MultipartFile image3
    ) throws DataException {
        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );
        Organization organization = organizationRepository.getReferenceById(request.getOrganizationId());
        AccessToPackagingLabellingAndBranding entity = RequestMapper.mapToAccessToPackagingLabellingAndBranding(request,organization,support);
        entity = repository.save(entity);
        if (image1 != null && !image1.isEmpty()) {
            String path = storageFiles(image1, entity.getAccessToPackagingId(), "AccessToPackaging");
            entity.setAleapDesignStudioImage1(path);
            repository.save(entity);

            programSessionFileRepository.save(
                    ProgramSessionFile.builder()
                            .fileType("File")
                            .filePath(path)
                            .accessToPackagingLabellingAndBranding(entity)
                            .build()
            );
        }

        if (image2 != null && !image2.isEmpty()) {
            String path = storageFiles(image2, entity.getAccessToPackagingId(), "AccessToPackaging");
            entity.setAleapDesignStudioImage2(path);
            repository.save(entity);

            programSessionFileRepository.save(
                    ProgramSessionFile.builder()
                            .fileType("File")
                            .filePath(path)
                            .accessToPackagingLabellingAndBranding(entity)
                            .build()
            );
        }

        // ---- IMAGE 3 ----
        if (image3 != null && !image3.isEmpty()) {
            String path = storageFiles(image3, entity.getAccessToPackagingId(), "AccessToPackaging");
            entity.setAleapDesignStudioImage3(path);
            repository.save(entity);

            programSessionFileRepository.save(
                    ProgramSessionFile.builder()
                            .fileType("File")
                            .filePath(path)
                            .accessToPackagingLabellingAndBranding(entity)
                            .build()
            );
        }

        AccessToPackagingLabellingAndBranding saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Access To Packaging, Labelling & Branding saved successfully")
                .data(ResponseMapper.mapToAccessToPackagingLabellingAndBrandingResponse(saved))
                .build();
    }

    @Transactional
    public WorkflowResponse update(
            Long id,
            AccessToPackagingLabellingAndBrandingRequest request,
            MultipartFile image1,
            MultipartFile image2,
            MultipartFile image3
    ) throws DataException {

        AccessToPackagingLabellingAndBranding existing = repository
                .findById(id)
                .orElseThrow(() -> new DataException(
                        "Record not found with id " + id,
                        "ACCESS_PACKAGING_NOT_FOUND",
                        400
                ));

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        existing.setHandholdingSupport(support);
        existing.setAccessToPackagingType(request.getAccessToPackagingType());
        existing.setDetails(request.getDetails());
        existing.setEventLocation(request.getEventLocation());
        existing.setOrganizedBy(request.getOrganizedBy());
        existing.setEventType(request.getEventType());

        if (request.getStudioAccessDate() != null) {
            existing.setStudioAccessDate(DateUtil.covertStringToDate(request.getStudioAccessDate()));
        }

        if (request.getEventDate() != null) {
            existing.setEventDate(DateUtil.covertStringToDate(request.getEventDate()));
        }

        // ---- images update ----
        if (image1 != null && !image1.isEmpty()) {
            String path = storageFiles(image1, existing.getAccessToPackagingId(), "AccessToPackaging");
            existing.setAleapDesignStudioImage1(path);
        }

        if (image2 != null && !image2.isEmpty()) {
            String path = storageFiles(image2, existing.getAccessToPackagingId(), "AccessToPackaging");
            existing.setAleapDesignStudioImage2(path);
        }

        if (image3 != null && !image3.isEmpty()) {
            String path = storageFiles(image3, existing.getAccessToPackagingId(), "AccessToPackaging");
            existing.setAleapDesignStudioImage3(path);
        }

        AccessToPackagingLabellingAndBranding updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Updated successfully")
                .data(ResponseMapper.mapToAccessToPackagingLabellingAndBrandingResponse(updated))
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {

        AccessToPackagingLabellingAndBranding entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Record not found with id " + id,
                        "ACCESS_PACKAGING_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToAccessToPackagingLabellingAndBrandingResponse(entity))
                .build();
    }

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<AccessToPackagingLabellingAndBrandingResponse> response =
                repository.findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                        .stream()
                        .map(ResponseMapper::mapToAccessToPackagingLabellingAndBrandingResponse)
                        .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    public WorkflowResponse delete(Long id) throws DataException {

        AccessToPackagingLabellingAndBranding entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Record not found with id " + id,
                        "ACCESS_PACKAGING_NOT_FOUND",
                        400
                ));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Deleted successfully")
                .build();
    }
}
