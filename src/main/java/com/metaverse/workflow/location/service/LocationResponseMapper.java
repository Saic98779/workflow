package com.metaverse.workflow.location.service;

import org.springframework.stereotype.Component;

import com.metaverse.workflow.model.Location;

@Component
public class LocationResponseMapper {


    public static LocationResponse map(Location location) {
        return LocationResponse.builder()
                .locationId(location.getLocationId())
                .district(location.getDistrict())
                .mandal(location.getMandal())
                .locationName(location.getLocationName())
                .ownershipType(location.getOwnershipType())
                .typeOfVenue(location.getTypeOfVenue())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .googleMapUrl(location.getGoogleMapUrl())
                .capacity(location.getCapacity())
                .filePath(location.getFilePath())
                .agencyId(location.getAgency().getAgencyId())
                .agencyName(location.getAgency().getAgencyName())
                .createdOn(location.getCreatedOn())
                .updatedOn(location.getUpdatedOn())
                .build();
    }
}
