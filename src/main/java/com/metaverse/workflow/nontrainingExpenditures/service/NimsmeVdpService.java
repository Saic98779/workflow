package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.NimsmeVDP;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NimsmeVdpRequest;
import com.metaverse.workflow.nontrainingExpenditures.repository.NimsmeVDPRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class NimsmeVdpService {

    private final NimsmeVDPRepository repository;
    private final NonTrainingSubActivityRepository subActivityRepository;
    private final OrganizationRepository organizationRepository;
    private final StorageService storageService;
    private final ProgramSessionFileRepository programSessionFileRepository;

    public String storageFiles(MultipartFile file, Long TravelAndTransportId, String folderName) {
        String filePath = storageService.store(file, TravelAndTransportId, folderName);
        return filePath;
    }
    public WorkflowResponse save(NimsmeVdpRequest request, MultipartFile file) throws DataException {

        NonTrainingSubActivity subActivity = subActivityRepository.findById(request.getSubActivityId())
                        .orElseThrow(()->new DataException("SubActivity not found with id " + request.getSubActivityId(), "SUB_ACTIVITY_NOT_FOUND", 400));
        Organization sellerOrg = organizationRepository.findById(request.getSellerOrganizationId())
                .orElseThrow(()->new DataException("Seller Organization not found with id " + request.getSellerOrganizationId(), "ORGANIZATION_NOT_FOUND", 400));
        Organization buyerOrg = organizationRepository.findById(request.getBuyerOrganizationId())
                .orElseThrow(()->new DataException("Buyer Organization not found with id " + request.getBuyerOrganizationId(), "ORGANIZATION_NOT_FOUND", 400));
        NimsmeVDP entity = NimsmeVdpMapper.mapToNimsmeVdp(request, subActivity, sellerOrg, buyerOrg);
        NimsmeVDP saved = repository.save(entity);
        if (file != null && !file.isEmpty()) {
            String filePath = this.storageFiles(file, saved.getNimsmeVdpId(), "NotTrainingNimsmeVdp");
            saved.setUploadParticipantDetails(filePath);
            repository.save(saved);
            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .nimsmeVDP(saved)
                    .filePath(filePath)
                    .build());
        }
        return WorkflowResponse.builder()
                .status(200)
                .message("NIMSME VDP created successfully")
                .data(NimsmeVdpMapper.mapToNimsmeResponse(saved))
                .build();
    }

    public WorkflowResponse update(Long id, NimsmeVdpRequest request, MultipartFile file) throws DataException {

        NimsmeVDP entity = repository.findById(id).orElseThrow(() -> new RuntimeException("NIMSME VDP not found"));
        Organization sellerOrg = organizationRepository.findById(request.getSellerOrganizationId())
                .orElseThrow(()->new DataException("Seller Organization not found with id " + request.getSellerOrganizationId(), "ORGANIZATION_NOT_FOUND", 400));
        Organization buyerOrg = organizationRepository.findById(request.getBuyerOrganizationId())
                .orElseThrow(()->new DataException("Buyer Organization not found with id " + request.getBuyerOrganizationId(), "ORGANIZATION_NOT_FOUND", 400));
        NimsmeVdpMapper.mapToUpdateNimsmeVdp(entity, request,sellerOrg, buyerOrg);

        NimsmeVDP updated= repository.save(entity);
        if (file != null && !file.isEmpty()) {
            String filePath = storageFiles(file, updated.getNimsmeVdpId(), "NotTrainingNimsmeVdp");
            updated.setUploadParticipantDetails(filePath);
            repository.save(updated);

            programSessionFileRepository.save(
                    ProgramSessionFile.builder()
                            .fileType("File")
                            .filePath(filePath)
                            .nimsmeVDP(updated)
                            .build()
            );
        }
        return WorkflowResponse.builder()
                .status(200)
                .message("NIMSME VDP updated successfully")
                .data(NimsmeVdpMapper.mapToNimsmeResponse(entity))
                .build();
    }


    public WorkflowResponse getById(Long id) throws DataException {
        NimsmeVDP entity = repository.findById(id)
                .orElseThrow(()->new DataException("Nimsme VDP not found with id " + id, "VDP_NOT_FOUND", 400));
        return WorkflowResponse.builder()
                .status(200)
                .message("NIMSME VDP fetched successfully")
                .data(NimsmeVdpMapper.mapToNimsmeResponse(entity))
                .build();
    }

    public WorkflowResponse getBySubActivityId(Long subActivityId) {
        return WorkflowResponse.builder()
                .status(200)
                .message("NIMSME VDP list fetched successfully")
                .data(
                        repository.findByNonTrainingSubActivity_SubActivityId(subActivityId)
                                .stream()
                                .map(NimsmeVdpMapper::mapToNimsmeResponse)
                                .toList()
                )
                .build();
    }
    @Transactional
    public WorkflowResponse delete(Long id) throws DataException {
        try {
            repository.findById(id)
                    .orElseThrow(() ->
                            new DataException(
                                    "NIMSME VDP not found with id " + id,
                                    "VDP_NOT_FOUND",
                                    400
                            )
                    );
            programSessionFileRepository.deleteByNimsmeVDP_NimsmeVdpId(id);
            repository.deleteById(id);
            return WorkflowResponse.builder()
                    .status(200)
                    .message("NIMSME VDP deleted successfully")
                    .build();

        } catch (DataException e) {
            throw e; // rethrow to be handled by controller/global handler
        } catch (Exception e) {
            throw new DataException(e.getMessage(), "VDP_DELETE_FAILED", 500);
        }
    }

}
