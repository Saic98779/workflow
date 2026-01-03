package com.metaverse.workflow.aleap_handholding.service;


import com.metaverse.workflow.aleap_handholding.repository.BusinessPlanDetailsRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.BusinessPlanRequest;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.model.aleap_handholding.BusinessPlanDetails;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessPlanDetailsService {

private final HandholdingSupportService service;
    private final BusinessPlanDetailsRepository repository;
    private final OrganizationRepository organizationRepo;
    private final ParticipantRepository participantRepo;
    private final StorageService storageService;
    private final ProgramSessionFileRepository programSessionFileRepository;

    public String storageFiles(MultipartFile file, Long TravelAndTransportId, String folderName) {
        String filePath = storageService.store(file, TravelAndTransportId, folderName);
        return filePath;
    }
    public WorkflowResponse save(BusinessPlanRequest request, MultipartFile file) throws DataException {

        HandholdingSupport support = service.getOrCreateSupport(request.getHandholdingSupportId(), request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(), request.getHandHoldingType());
        Organization organization = organizationRepo.findById(request.getOrganizationId())
                .orElseThrow(() ->
                        new DataException(
                                "Organization not found with id " + request.getOrganizationId(),
                                "ORG_NOT_FOUND", 400
                        )
                );
        List<Participant> participants = participantRepo.findAllById(request.getParticipantIds());
        BusinessPlanDetails entity = RequestMapper.mapToBusinessPlan(request, support, organization, participants);
        BusinessPlanDetails saved = repository.save(entity);
        if (file != null && !file.isEmpty()) {
            String filePath = this.storageFiles(file, saved.getBusinessPlanDetailsId(), "NotTrainingBusinessPlan");
            saved.setPlanFileUploadPath(filePath);
            repository.save(saved);
            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .filePath(filePath)
                    .businessPlanDetails(saved)
                    .build());
        }
        return WorkflowResponse.builder()
                .status(200)
                .message("Business Plan details saved successfully")
                .data(ResponseMapper.mapToBusinessPlanResponse(saved))
                .build();
    }
    public WorkflowResponse update(Long id, BusinessPlanRequest request, MultipartFile file) throws DataException {

        BusinessPlanDetails existing = repository.findById(id)
                .orElseThrow(() ->
                        new DataException("Business Plan not found with id " + id, "BUSINESS_PLAN_NOT_FOUND", 400)
                );

        HandholdingSupport support = service.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        Organization organization = organizationRepo.findById(request.getOrganizationId())
                .orElseThrow(() ->
                        new DataException(
                                "Organization not found with id " + request.getOrganizationId(),
                                "ORG_NOT_FOUND", 400
                        )
                );

        List<Participant> participants = participantRepo.findAllById(request.getParticipantIds());

        existing.setHandholdingSupport(support);
        existing.setOrganization(organization);
        existing.setParticipants(participants);
        existing.setBankName(request.getBankName());
        existing.setBranchName(request.getBranchName());
        existing.setBankRemarks(request.getBankRemarks());
        existing.setCounsellingDate(DateUtil.covertStringToDate(request.getCounsellingDate()));
        existing.setCounsellingTime(request.getCounsellingTime());
        existing.setCounselledBy(request.getCounselledBy());

        BusinessPlanDetails updated = repository.save(existing);

        if (file != null && !file.isEmpty()) {
            String filePath = storageFiles(file, updated.getBusinessPlanDetailsId(), "NotTrainingBusinessPlan");
            updated.setPlanFileUploadPath(filePath);
            repository.save(updated);

            programSessionFileRepository.save(
                    ProgramSessionFile.builder()
                            .fileType("File")
                            .filePath(filePath)
                            .businessPlanDetails(updated)
                            .build()
            );
        }

        return WorkflowResponse.builder().status(200)
                .message("Business Plan updated successfully")
                .data(ResponseMapper.mapToBusinessPlanResponse(updated))
                .build();
    }

    public WorkflowResponse getById(Long id) throws DataException {
        BusinessPlanDetails entity = repository.findById(id)
                .orElseThrow(() ->
                        new DataException(
                                "Business Plan not found with id " + id,
                                "BUSINESS_PLAN_NOT_FOUND", 404
                        )
                );
        return WorkflowResponse.builder().status(200)
                .message("Business Plan fetched successfully")
                .data(ResponseMapper.mapToBusinessPlanResponse(entity))
                .build();
    }
    public WorkflowResponse delete(Long id) throws DataException {

        BusinessPlanDetails entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                                "Business Plan not found with id " + id,
                                "BUSINESS_PLAN_NOT_FOUND", 404
                        )
                );
        repository.delete(entity);
        return WorkflowResponse.builder()
                .status(200)
                .message("Business Plan deleted successfully")
                .build();
    }

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<BusinessPlanDetails> list =
                repository.findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId);
        return WorkflowResponse.builder()
                .status(200)
                .message("Business Plans fetched successfully")
                .data(
                        list.stream()
                                .map(ResponseMapper::mapToBusinessPlanResponse)
                                .toList()
                )
                .build();
    }







}
