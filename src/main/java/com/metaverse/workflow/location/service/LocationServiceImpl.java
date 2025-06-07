package com.metaverse.workflow.location.service;

import com.metaverse.workflow.agency.service.AgencyService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.location.repository.LocationRepository;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Location;
import com.metaverse.workflow.program.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private LocationRequestMapper requestMapper;

    @Autowired
    ProgramRepository programRepository;

    @Override
    public LocationResponse saveLocation(LocationRequest locationRequest) {
        Agency agency = agencyService.getAgencyById(locationRequest.getAgencyId());
        if (agency == null) return null;
        Location location = requestMapper.map(locationRequest);
        List<Location> locations = agency.getLocations();
        locations.add(location);
        agency.setLocations(locations);
        location.setAgency(agency);
        Location savedLocation = locationRepository.save(location);
        return LocationResponseMapper.map(savedLocation);
    }

    @Override
    public List<LocationResponse> getLocationByAgencyId(Long agencyId) {
        List<Location> locationList = locationRepository.findByAgencyAgencyId(agencyId);
        ArrayList<LocationResponse> LocationResponseList = new ArrayList<LocationResponse>();
        for (Location location : locationList) {
            LocationResponseList.add(LocationResponseMapper.map(location));
        }
        return LocationResponseList;
    }

    @Override
    public WorkflowResponse getLocations() {
        List<Location> locationList = locationRepository.findAll();
        List<LocationResponse> response = locationList != null ? locationList.stream().map(location -> LocationResponseMapper.map(location)).collect(Collectors.toList()) : null;
        return WorkflowResponse.builder().message("Success").status(200).data(response).build();

    }

    @Override
    public LocationResponse updateLocation(Long locationId, LocationRequest locationRequest) throws DataException {

        Location existingLocation = locationRepository.findById(locationId)
                .orElseThrow(() -> new DataException("Location with ID " + locationId + " not found", "LOCATION_NOT_FOUND", 400));

        existingLocation.setLocationName(locationRequest.getLocationName());
        existingLocation.setOwnershipType(locationRequest.getOwnershipType());
        existingLocation.setTypeOfVenue(locationRequest.getTypeOfVenue());
        existingLocation.setDistrict(locationRequest.getDistrict());
        existingLocation.setMandal(locationRequest.getMandal());
        existingLocation.setLatitude(locationRequest.getLatitude());
        existingLocation.setLongitude(locationRequest.getLongitude());
        existingLocation.setGoogleMapUrl(locationRequest.getGoogleMapUrl());
        existingLocation.setCapacity(locationRequest.getCapacity());
        existingLocation.setFilePath(locationRequest.getFilePath());


        if (!existingLocation.getAgency().getAgencyId().equals(locationRequest.getAgencyId())) {
            Agency agency = agencyService.getAgencyById(locationRequest.getAgencyId());
            if (agency == null) {
                throw new DataException("Agency with ID " + locationRequest.getAgencyId() + " not found", "AGENCY_NOT_FOUND");
            }
            existingLocation.setAgency(agency);
        }
        return LocationResponseMapper.map(locationRepository.save(existingLocation));
    }

    @Override
    public WorkflowResponse deleteLocation(Long locationId) throws DataException {
        Location existingLocation = locationRepository.findById(locationId)
                .orElseThrow(() -> new DataException("Location with ID " + locationId + " not found", "LOCATION_NOT_FOUND", 400));

        if (programRepository.existsByLocation_LocationId(locationId)) {
            return WorkflowResponse.builder().status(400)
                    .message("Cannot delete : Program already exist for location with ID" + locationId).build();
        }
        locationRepository.deleteById(locationId);
        return WorkflowResponse.builder().message("Location deleted successfully").status(200).build();
         
    }


}
