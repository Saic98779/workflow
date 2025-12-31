package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.AleapDesignStudioRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.AleapDesignStudioRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.AleapDesignStudioResponse;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.model.aleap_handholding.AleapDesignStudio;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AleapDesignStudioService {

    private final StorageService storageService;
    private final HandholdingSupportService service;
    private final AleapDesignStudioRepository repository;
    private final ProgramSessionFileRepository programSessionFileRepository;

    public String storageFiles(MultipartFile file, Long TravelAndTransportId, String folderName) {
        String filePath = storageService.store(file, TravelAndTransportId, folderName);
        return filePath;
    }
    @Transactional
    public WorkflowResponse save(
            AleapDesignStudioRequest request,
            MultipartFile image1,
            MultipartFile image2,
            MultipartFile image3) throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        AleapDesignStudio entity =
                RequestMapper.mapToAleapDesignStudio(request, support);

        // Save parent first
        entity = repository.save(entity);

        if (image1 != null && !image1.isEmpty()) {
            String filePath = this.storageFiles(image1, entity.getAleapDesignStudioId(), "NotTrainingAleapDeignStudio");
            entity.setAleapDesignStudioImage1(filePath);
            repository.save(entity);
            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .filePath(filePath)
                    .aleapDesignStudio(entity)
                    .build());
        }
        if (image2 != null && !image2.isEmpty()) {
            String filePath = this.storageFiles(image1, entity.getAleapDesignStudioId(), "NotTrainingAleapDeignStudio");
            entity.setAleapDesignStudioImage1(filePath);
            repository.save(entity);
            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .filePath(filePath)
                    .aleapDesignStudio(entity)
                    .build());
        }
        if (image3 != null && !image3.isEmpty()) {
            String filePath = this.storageFiles(image1, entity.getAleapDesignStudioId(), "NotTrainingAleapDeignStudio");
            entity.setAleapDesignStudioImage1(filePath);
            repository.save(entity);
            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .filePath(filePath)
                    .aleapDesignStudio(entity)
                    .build());
        }

        AleapDesignStudio saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Aleap Design Studio saved successfully")
                .data(ResponseMapper.mapToAleapDesignStudioResponse(saved))
                .build();
    }
    @Transactional
    public WorkflowResponse update(Long id, AleapDesignStudioRequest request, MultipartFile image1,
            MultipartFile image2, MultipartFile image3) throws DataException {

        AleapDesignStudio existing = repository.findById(id).orElseThrow(() -> new DataException(
                        "Aleap Design Studio not found with id " + id, "DESIGN_STUDIO_NOT_FOUND", 400));

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(), request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(), request.getHandHoldingType()
        );
        existing.setHandholdingSupport(support);
        existing.setDetails(request.getDetails());
        if (request.getStudioAccessDate() != null) {
            existing.setStudioAccessDate(
                    DateUtil.covertStringToDate(request.getStudioAccessDate())
            );
        }
        if (image1 != null && !image1.isEmpty()) {
            String filePath = this.storageFiles(
                    image1,
                    existing.getAleapDesignStudioId(),
                    "NotTrainingAleapDeignStudio"
            );
            existing.setAleapDesignStudioImage1(filePath);
            programSessionFileRepository.save(
                    ProgramSessionFile.builder()
                            .fileType("File")
                            .filePath(filePath)
                            .aleapDesignStudio(existing)
                            .build()
            );
        }
        if (image2 != null && !image2.isEmpty()) {
            String filePath = this.storageFiles(
                    image2,
                    existing.getAleapDesignStudioId(),
                    "NotTrainingAleapDeignStudio"
            );
            existing.setAleapDesignStudioImage2(filePath);
            programSessionFileRepository.save(
                    ProgramSessionFile.builder()
                            .fileType("File")
                            .filePath(filePath)
                            .aleapDesignStudio(existing)
                            .build()
            );
        }

        if (image3 != null && !image3.isEmpty()) {
            String filePath = this.storageFiles(
                    image3,
                    existing.getAleapDesignStudioId(),
                    "NotTrainingAleapDeignStudio"
            );
            existing.setAleapDesignStudioImage3(filePath);
            programSessionFileRepository.save(
                    ProgramSessionFile.builder()
                            .fileType("File")
                            .filePath(filePath)
                            .aleapDesignStudio(existing)
                            .build()
            );
        }
        AleapDesignStudio updated = repository.save(existing);
        return WorkflowResponse.builder()
                .status(200)
                .message("Aleap Design Studio updated successfully")
                .data(ResponseMapper.mapToAleapDesignStudioResponse(updated))
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {

        AleapDesignStudio entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Aleap Design Studio not found with id " + id,
                        "DESIGN_STUDIO_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToAleapDesignStudioResponse(entity))
                .build();
    }

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<AleapDesignStudioResponse> response = repository
                .findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                .stream()
                .map(ResponseMapper::mapToAleapDesignStudioResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
    }

    public WorkflowResponse delete(Long id) throws DataException {

        AleapDesignStudio entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Aleap Design Studio not found with id " + id,
                        "DESIGN_STUDIO_NOT_FOUND",
                        400
                ));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Aleap Design Studio deleted successfully")
                .build();
    }
}
