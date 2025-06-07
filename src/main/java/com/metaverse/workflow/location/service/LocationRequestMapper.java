package com.metaverse.workflow.location.service;

import org.springframework.stereotype.Component;

import com.metaverse.workflow.model.Location;

@Component
public class LocationRequestMapper {

    public Location map(LocationRequest request) {
        return Location.builder().agency(request.getAgency())
                .locationName(request.getLocationName()).ownershipType(request.getOwnershipType())
                .typeOfVenue(request.getTypeOfVenue()).latitude(request.getLatitude())
                .longitude(request.getLongitude()).googleMapUrl(request.getGoogleMapUrl())
                .district(request.getDistrict())
                .mandal(request.getMandal())
                .capacity(request.getCapacity()).filePath(request.getFilePath())
                .build();

    }
}
