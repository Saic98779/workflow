package com.metaverse.workflow.location.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {
	
	public LocationResponse saveLocation(LocationRequest location);
	
	public List<LocationResponse> getLocationByAgencyId(Long agencyId);

	public WorkflowResponse getLocations();
	LocationResponse updateLocation(Long locationId, LocationRequest locationRequest) throws DataException;
	WorkflowResponse deleteLocation(Long locationId) throws DataException;
}
