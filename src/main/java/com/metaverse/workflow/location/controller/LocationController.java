package com.metaverse.workflow.location.controller;

import java.security.Principal;
import java.util.List;

import com.metaverse.workflow.activitylog.ActivityLogService;
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
	@Autowired
	private ActivityLogService logService;
	
	@PostMapping("/location/save")
	public ResponseEntity<WorkflowResponse> saveLocation(@RequestBody LocationRequest location, Principal principal)
	{
	LocationResponse response = locationSercice.saveLocation(location);
		if(response==null)return  ResponseEntity.internalServerError().body(WorkflowResponse.builder().message("Invalid Agency Id").status(400).build());
		logService.logs(principal.getName(), "save","location creation","location","/location/save");
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
	public ResponseEntity<?> updateLocation(Principal principal,@PathVariable Long locationId, @RequestBody LocationRequest locationRequest) {
		try {
			LocationResponse response =locationSercice.updateLocation(locationId, locationRequest);
			logService.logs(principal.getName(), "update","location update","location","/locations/update/{locationId}");
			return ResponseEntity.ok(response);
		} catch (DataException e) {
			return RestControllerBase.error(e);
		}
	}
	@DeleteMapping("locations/delete/{locationId}")
	public ResponseEntity<?> deleteLocation(@PathVariable Long locationId,Principal principal) {
		try {
			WorkflowResponse  response = locationSercice.deleteLocation(locationId);
			logService.logs(principal.getName(), "delete","delete location","location","locations/delete/{locationId}");
			return ResponseEntity.ok(response);
		} catch (DataException e) {
			return RestControllerBase.error(e);
		}
	}


}
