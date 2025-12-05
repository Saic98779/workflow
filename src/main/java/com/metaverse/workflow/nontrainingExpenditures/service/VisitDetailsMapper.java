package com.metaverse.workflow.nontrainingExpenditures.service;


import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.*;
import java.util.List;
import java.util.stream.Collectors;

public class VisitDetailsMapper {

    public static VisitDetails mapToEntity(VisitDetailsRequest request, Organization organization, NonTrainingSubActivity subActivity, List<NonTrainingResource> resourceList) {
        VisitDetails visitDetails = VisitDetails.builder()
                .organization(organization)
                .nonTrainingSubActivity(subActivity)
                .dateOfVisit(DateUtil.covertStringToDate(request.getDateOfVisit()))
                .timeOfVisit(request.getTimeOfVisit())
                .build();

        resourceList.forEach(r -> r.setVisitDetails(visitDetails));
        visitDetails.setNonTrainingResourceList(resourceList);
        return visitDetails;

    }

    public static void mapToUpdate(VisitDetails entity, VisitDetailsRequest request,  List<NonTrainingResource> resourceList) {
        entity.setDateOfVisit(DateUtil.covertStringToDate(request.getDateOfVisit()));
        entity.setTimeOfVisit(request.getTimeOfVisit());
        entity.setNonTrainingResourceList(resourceList);

    }

    public static VisitDetailsResponseDTO mapToResponse(VisitDetails entity) {

        VisitDetailsResponseDTO dto = new VisitDetailsResponseDTO();
        dto.setVisitDetailsId(entity.getId());

        // organization details
        if (entity.getOrganization() != null) {
            dto.setOrganizationId(entity.getOrganization().getOrganizationId());
            dto.setOrganizationName(entity.getOrganization().getOrganizationName());
        }

        // sub-activity details
        if (entity.getNonTrainingSubActivity() != null) {
            dto.setSubActivityId(entity.getNonTrainingSubActivity().getSubActivityId());
            dto.setSubActivityName(entity.getNonTrainingSubActivity().getSubActivityName());
        }

        dto.setDateOfVisit(entity.getDateOfVisit());
        dto.setTimeOfVisit(entity.getTimeOfVisit());

        // convert NonTrainingResource list → list of only names
        dto.setResourceNames(entity.getNonTrainingResourceList().stream()
                .map(NonTrainingResource::getName)
                .collect(Collectors.toList()));

        return dto;
    }
}
