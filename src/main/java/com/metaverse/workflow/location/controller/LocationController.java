package com.metaverse.workflow.location.controller;

import java.util.List;

import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.location.service.LocationRequest;
import com.metaverse.workflow.location.service.LocationResponse;
import com.metaverse.workflow.location.service.LocationService;

@RestController
public class LocationController {
	@Autowired
	private LocationService locationSercice;
	
	@PostMapping("/location/save")
	public ResponseEntity<WorkflowResponse> saveLocation(@RequestBody LocationRequest location)
	{
	LocationResponse response = locationSercice.saveLocation(location);
		if(response==null)return  ResponseEntity.internalServerError().body(WorkflowResponse.builder().message("Invalid Agency Id").status(400).build());
		return ResponseEntity.ok(WorkflowResponse.builder().status(200).message("Created").data(response).build());
	}
	
	@GetMapping("/location/{id}")
	public ResponseEntity<WorkflowResponse> getLocationByAgencyId(@PathVariable("id")Long id)
	{
		List<LocationResponse> locationresponces = locationSercice.getLocationByAgencyId(id);
		return ResponseEntity.ok(WorkflowResponse.builder().message("Success").status(200).data(locationresponces).build());
	}

	@GetMapping("/locations")
	public ResponseEntity<WorkflowResponse> getLocations()
	{
		WorkflowResponse response = locationSercice.getLocations();
		return ResponseEntity.ok(response);
	}
	@PutMapping("/locations/update/{locationId}")
	public ResponseEntity<?> updateLocation(@PathVariable Long locationId, @RequestBody LocationRequest locationRequest) {
		try {
			return ResponseEntity.ok(locationSercice.updateLocation(locationId, locationRequest));
		} catch (DataException e) {
			return RestControllerBase.error(e);
		}
	}
	@DeleteMapping("locations/delete/{locationId}")
	public ResponseEntity<?> deleteLocation(@PathVariable Long locationId) {
		try {
			return ResponseEntity.ok(locationSercice.deleteLocation(locationId));
		} catch (DataException e) {
			return RestControllerBase.error(e);
		}
	}


}
