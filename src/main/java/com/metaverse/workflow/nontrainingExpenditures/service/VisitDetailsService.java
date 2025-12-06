package com.metaverse.workflow.nontrainingExpenditures.service;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.NonTrainingResource;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.VisitDetails;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingResourceRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.VisitDetailsRepository;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitDetailsService  {

    private final VisitDetailsRepository visitDetailsRepository;
    private final OrganizationRepository organizationRepository;
    private final NonTrainingSubActivityRepository subActivityRepository;
    private final NonTrainingResourceRepository resourceRepository;


    public WorkflowResponse save(VisitDetailsRequest request) throws DataException {

        Organization org = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new DataException("Organization not found", "ORG_NOT_FOUND", 400));

        NonTrainingSubActivity sub = subActivityRepository.findById(request.getSubActivityId())
                .orElseThrow(() -> new DataException("Sub Activity not found", "SUB_ACTIVITY_NOT_FOUND", 400));

        List<NonTrainingResource> resources = new ArrayList<>();

        for (Long id : request.getNonTrainingResourceIds()) {
            Optional<NonTrainingResource> optional = resourceRepository.findById(id);
            if (optional.isEmpty()) {
                throw new DataException("Resource not found with id: " + id,
                        "RESOURCE_NOT_FOUND",
                        400);
            }
            resources.add(optional.get());
        }


        VisitDetails entity = VisitDetailsMapper.mapToEntity(request, org, sub, resources);
        VisitDetails saved = visitDetailsRepository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Visit Details saved successfully")
                .data(VisitDetailsMapper.mapToResponse(saved))
                .build();
    }



    public WorkflowResponse update(Long id, VisitDetailsRequest request) throws DataException {

        VisitDetails entity = visitDetailsRepository.findById(id)
                .orElseThrow(() -> new DataException("VisitDetails not found", "VISIT_DETAILS_NOT_FOUND", 400));

        List<NonTrainingResource> resources = new ArrayList<>();

        for (Long resourceId : request.getNonTrainingResourceIds()) {
            Optional<NonTrainingResource> optional = resourceRepository.findById(resourceId);
            if (optional.isEmpty()) {
                throw new DataException("Resource not found with id: " + resourceId,
                        "RESOURCE_NOT_FOUND",
                        400);
            }
            resources.add(optional.get());
        }
        VisitDetailsMapper.mapToUpdate(entity, request, resources);
        VisitDetails updated = visitDetailsRepository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Visit Details updated successfully")
                .data(VisitDetailsMapper.mapToResponse(updated))
                .build();
    }



    public WorkflowResponse delete(Long id) throws DataException {

        VisitDetails entity = visitDetailsRepository.findById(id)
                .orElseThrow(() -> new DataException("VisitDetails not found", "VISIT_DETAILS_NOT_FOUND", 400));

        visitDetailsRepository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Visit Details deleted successfully")
                .build();
    }



    public WorkflowResponse getVisitDetailsById(Long id) throws DataException {

        VisitDetails entity = visitDetailsRepository.findById(id)
                .orElseThrow(() -> new DataException("VisitDetails not found", "VISIT_DETAILS_NOT_FOUND", 400));

        return WorkflowResponse.builder()
                .status(200)
                .message("Visit Details fetched successfully")
                .data(VisitDetailsMapper.mapToResponse(entity))
                .build();
    }


    public WorkflowResponse getBySubActivityId(Long subActivityId) {

        List<VisitDetails> list = visitDetailsRepository.findByNonTrainingSubActivitySubActivityId(subActivityId);

        List<VisitDetailsResponseDTO> responseDTOList = list.stream()
                .map(VisitDetailsMapper::mapToResponse)
                .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Visit Details fetched successfully")
                .data(responseDTOList)
                .build();
    }

}
