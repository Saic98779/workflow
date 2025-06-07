package com.metaverse.workflow.location.service;

import java.util.Date;

import com.metaverse.workflow.model.Agency;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LocationRequest {

	private Long locationId;
	private String locationName;
	private String ownershipType;
	private String typeOfVenue;
	private String district;
	private String mandal;
	private Double latitude;
	private Double longitude;
	private String googleMapUrl;
	private Integer capacity;
	private String filePath;
	private Agency agency;
	private Date createdOn;
	private Date updatedOn;
	private Long agencyId;
	

}
