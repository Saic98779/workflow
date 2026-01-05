package com.metaverse.workflow.aleap_handholding.service;


import com.metaverse.workflow.aleap_handholding.repository.FormalisationComplianceRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.FormalisationComplianceRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.FormalisationComplianceResponse;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.model.aleap_handholding.FormalisationCompliance;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormalisationComplianceServiceImpl implements FormalisationComplianceService {

    private final FormalisationComplianceRepository repository;
    private final NonTrainingSubActivityRepository subActivityRepository;
    private final NonTrainingActivityRepository activityRepository;
    private final OrganizationRepository organizationRepository;
    private final StorageService storageService;
    private final ProgramSessionFileRepository programSessionFileRepository;



    @Override
    public WorkflowResponse create(FormalisationComplianceRequest request, MultipartFile file) {

        try {
            NonTrainingSubActivity subActivity = subActivityRepository.findById(request.getNonTrainingSubActivityId())
                    .orElseThrow(() -> new RuntimeException("Sub Activity not found"));

            NonTrainingActivity activity = activityRepository.findById(request.getNonTrainingActivityId())
                    .orElseThrow(() -> new RuntimeException("Activity not found"));

            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));

            FormalisationCompliance entity = new FormalisationCompliance();
            entity.setNonTrainingSubActivity(subActivity);
            entity.setNonTrainingActivity(activity);
            entity.setOrganization(organization);
            entity.setDocumentPath(request.getDocumentPath());
            entity.setDetails(request.getDetails());
            FormalisationCompliance save = repository.save(entity);

            if (file != null && !file.isEmpty()) {
                String filePath = storageService.store(file, save.getFormalisationComplianceId(), "FormalisationCompliance");
                save.setDocumentPath(filePath);
                repository.save(save);
                programSessionFileRepository.save(ProgramSessionFile.builder()
                        .fileType("File")
                        .filePath(filePath)
                        .formalisationCompliance(save)
                        .build());
            }
            return WorkflowResponse.success("Formalisation Compliance created successfully", mapToResponse(entity));


        } catch (Exception e) {
            return WorkflowResponse.error(e.getMessage());
        }

    }

    // UPDATE
    @Override
    public WorkflowResponse update(Long id, FormalisationComplianceRequest request, MultipartFile file) {

        try {
            FormalisationCompliance entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Formalisation Compliance not found"));

            entity.setDetails(request.getDetails());

            if (request.getNonTrainingSubActivityId() != null) {
                NonTrainingSubActivity subActivity = subActivityRepository.findById(request.getNonTrainingSubActivityId()).orElseThrow(() -> new RuntimeException("Sub Activity not found with id " + request.getNonTrainingSubActivityId()));
            }

            if (request.getNonTrainingActivityId() != null) {
                NonTrainingActivity activity = activityRepository.findById(request.getNonTrainingActivityId()).orElseThrow(() -> new RuntimeException("Activity not found with id " + request.getNonTrainingActivityId()));
            }

            if (request.getOrganizationId() != null) {
                Organization organization = organizationRepository.findById(request.getOrganizationId()).orElseThrow(() -> new RuntimeException("Organization not found with id " + request.getOrganizationId()));
            }

            repository.save(entity);

            if (file != null && !file.isEmpty()) {
                String filePath = storageService.store(file, entity.getFormalisationComplianceId(), "FormalisationCompliance");

                entity.setDocumentPath(filePath);
                repository.save(entity);

                programSessionFileRepository.save(ProgramSessionFile.builder().fileType("File").filePath(filePath).formalisationCompliance(entity).build());
            }

            FormalisationComplianceResponse response =
                    mapToResponse(entity);

            return WorkflowResponse.success("Formalisation Compliance updated successfully", response);
        } catch (Exception e) {
            return WorkflowResponse.error(e.getMessage());
        }
    }


    // GET BY ID
    @Override
    public WorkflowResponse getById(Long id) {

        FormalisationCompliance entity = repository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Record not found"));

        return WorkflowResponse.success("Data fetched successfully", mapToResponse(entity));
    }

    // DELETE
    @Transactional
    @Override
    public WorkflowResponse delete(Long id) {

        FormalisationCompliance entity =
                repository.findById(id)
                        .orElse(null);

        if (entity == null) {
            return WorkflowResponse.error("Record not found with Id: " + id);
        }

        if (entity.getDocumentPath() != null) {
            storageService.deleteAll(List.of(entity.getDocumentPath()));
        }

        programSessionFileRepository.deleteByFormalisationCompliance_FormalisationComplianceId(id);

        repository.delete(entity);

        return WorkflowResponse.success("Formalisation Compliance and related file deleted successfully");
    }



    @Override
    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<FormalisationCompliance> entities = repository.findByNonTrainingSubActivity_SubActivityId(subActivityId);

        if (entities.isEmpty()) {
            return WorkflowResponse.error("No Formalisation Compliance found for Sub Activity ID: " + subActivityId);
        }

        List<FormalisationComplianceResponse> responseList =
                entities.stream()
                        .map(this::mapToResponse)
                        .toList();

        return WorkflowResponse.success("Data fetched successfully", responseList);
    }


    private FormalisationComplianceResponse mapToResponse(FormalisationCompliance entity) {

        return FormalisationComplianceResponse.builder()
                .formalisationComplianceId(entity.getFormalisationComplianceId())

                .nonTrainingSubActivityId(entity.getNonTrainingSubActivity().getSubActivityId())
                .nonTrainingSubActivityName(entity.getNonTrainingSubActivity().getSubActivityName())

                .nonTrainingActivityId(entity.getNonTrainingActivity().getActivityId())
                .nonTrainingActivityName(entity.getNonTrainingActivity().getActivityName())

                .organizationId(entity.getOrganization().getOrganizationId())
                .organizationName(entity.getOrganization().getOrganizationName())

                .documentPath(entity.getDocumentPath())
                .details(entity.getDetails())
                .build();
    }
}

