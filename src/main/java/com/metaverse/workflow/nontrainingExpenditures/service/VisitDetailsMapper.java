package com.metaverse.workflow.nontrainingExpenditures.service;


import com.metaverse.workflow.common.util.CommonUtil;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.NonTrainingResource;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.VisitDetails;

import java.util.List;
import java.util.stream.Collectors;

public class VisitDetailsMapper {

    public static VisitDetails mapToEntity(
            VisitDetailsRequest request,
            Organization organization,
            NonTrainingSubActivity subActivity,
            List<NonTrainingResource> resourceList) {

        VisitDetails visitDetails = VisitDetails.builder()
                .organization(organization)
                .nonTrainingSubActivity(subActivity)
                .dateOfVisit(DateUtil.covertStringToDate(request.getDateOfVisit()))
                .timeOfVisit(request.getTimeOfVisit())
                .state(request.getState())
                .district(request.getDistrict())
                .mandal(request.getMandal())
                .town(request.getTown())
                .houseNo(request.getHouseNo())
                .streetNo(request.getStreetNo())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .email(request.getEmail())
                .contactNo(request.getContactNo())
                .withInHyderabad(request.getWithInHyderabad())
                .build();


        if (resourceList != null && !resourceList.isEmpty()) {
            visitDetails.setNonTrainingResourceList(resourceList);
        }

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
        dto.setState(entity.getState());
        dto.setDistrict(entity.getDistrict());
        dto.setMandal(entity.getMandal());
        dto.setTown(entity.getTown());
        dto.setHouseNo(entity.getHouseNo());
        dto.setStreetNo(entity.getStreetNo());
        dto.setEmail(entity.getEmail());
        dto.setContactNo(entity.getContactNo());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setWithInHyderabad(entity.getWithInHyderabad());
        dto.setDistrictName(entity.getDistrict() != null ? CommonUtil.districtMap.get(Integer.valueOf(entity.getDistrict())) :null );
        dto.setMandalName(entity.getMandal() != null ? CommonUtil.mandalMap.get(Integer.valueOf(entity.getMandal())) :null );

        // convert NonTrainingResource list → list of only names
        dto.setResourceNames(entity.getNonTrainingResourceList().stream()
                .map(NonTrainingResource::getName)
                .collect(Collectors.toList()));

        return dto;
    }
}
