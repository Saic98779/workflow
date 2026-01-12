package com.metaverse.workflow.aleap_handholding.service;

import com.metaverse.workflow.aleap_handholding.repository.AccessToTechnologyAndInfrastructureRepository;
import com.metaverse.workflow.aleap_handholding.request_dto.AccessToTechnologyAndInfrastructureRequest;
import com.metaverse.workflow.aleap_handholding.response_dto.AccessToTechnologyAndInfrastructureResponse;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.aleap_handholding.AccessToTechnologyAndInfrastructure;
import com.metaverse.workflow.model.aleap_handholding.HandholdingSupport;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessToTechnologyAndInfrastructureService {
    private final HandholdingSupportService supportService;
    private final AccessToTechnologyAndInfrastructureRepository repository;
    private final OrganizationRepository organizationRepo;


    public WorkflowResponse save(AccessToTechnologyAndInfrastructureRequest request) throws DataException {

        HandholdingSupport support = supportService.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );

        Organization organization = null;
        if (request.getOrganizationId() != null) {
            organization = organizationRepo.getReferenceById(request.getOrganizationId());
        }

        AccessToTechnologyAndInfrastructure entity =
                RequestMapper.mapToAccessToTechnologyAndInfrastructure(request, support, organization);

        AccessToTechnologyAndInfrastructure saved = repository.save(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Access to Technology and Infrastructure saved successfully")
                .data(ResponseMapper.mapToAccessToTechnologyAndInfrastructureResponse(saved))
                .build();
    }
    @Transactional
    public WorkflowResponse update(Long id, AccessToTechnologyAndInfrastructureRequest request) throws DataException {

        AccessToTechnologyAndInfrastructure existing = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Access to Technology and Infrastructure not found with id " + id,
                        "ACCESS_TO_TECH_INFRA_NOT_FOUND",
                        404
                ));

        HandholdingSupport support = supportService.getOrCreateSupport(
                request.getHandholdingSupportId(),
                request.getNonTrainingActivityId(),
                request.getNonTrainingSubActivityId(),
                request.getHandHoldingType()
        );
        existing.setHandholdingSupport(support);

        if (request.getOrganizationId() != null) {
            existing.setOrganization(
                    organizationRepo.getReferenceById(request.getOrganizationId())
            );
        }

        // --- Vendor Connection fields ---
        existing.setVendorSuggested(request.getVendorSuggested());
        existing.setQuotationDate(DateUtil.covertStringToDate(request.getQuotationDate()));
        existing.setDetails(request.getDetails());
        existing.setCost(request.getCost());

        // --- Machinery Identification fields ---
        existing.setRequirement(request.getRequirement());
        existing.setExistingMachinery(request.getExistingMachinery());
        existing.setSuggestedMachinery(request.getSuggestedMachinery());
        existing.setManufacturer(request.getManufacturer());
        existing.setGroundingDate(DateUtil.covertStringToDate(request.getGroundingDate()));
        existing.setPlaceOfInstallation(request.getPlaceOfInstallation());
        existing.setCostOfMachinery(request.getCostOfMachinery());

        // --- CFC Support fields ---
        existing.setTechnologyDetails(request.getTechnologyDetails());
        existing.setVendorName(request.getVendorName());
        existing.setVendorContactNo(request.getVendorContactNo());
        existing.setVendorEmail(request.getVendorEmail());
        existing.setApproxCost(request.getApproxCost());

        AccessToTechnologyAndInfrastructure updated = repository.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Access to Technology and Infrastructure updated successfully")
                .data(ResponseMapper.mapToAccessToTechnologyAndInfrastructureResponse(updated))
                .build();
    }

    /* ---------------- GET BY ID ---------------- */

    public WorkflowResponse getById(Long id) throws DataException {

        AccessToTechnologyAndInfrastructure entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Access to Technology and Infrastructure not found with id " + id,
                        "ACCESS_TO_TECH_INFRA_NOT_FOUND",
                        404
                ));

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ResponseMapper.mapToAccessToTechnologyAndInfrastructureResponse(entity))
                .build();
    }

    /* ---------------- LIST BY SUB ACTIVITY ---------------- */

    public WorkflowResponse getByNonTrainingSubActivityId(Long subActivityId) {

        List<AccessToTechnologyAndInfrastructureResponse> list =
                repository.findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(subActivityId)
                        .stream()
                        .map(ResponseMapper::mapToAccessToTechnologyAndInfrastructureResponse)
                        .toList();

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(list)
                .build();
    }

    /* ---------------- DELETE ---------------- */

    public WorkflowResponse delete(Long id) throws DataException {

        AccessToTechnologyAndInfrastructure entity = repository.findById(id)
                .orElseThrow(() -> new DataException(
                        "Access to Technology and Infrastructure not found with id " + id,
                        "ACCESS_TO_TECH_INFRA_NOT_FOUND",
                        404
                ));

        repository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("Deleted successfully")
                .build();
    }

}
