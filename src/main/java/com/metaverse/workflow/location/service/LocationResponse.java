package com.metaverse.workflow.location.service;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.metaverse.workflow.model.Agency;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationResponse {

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
    private Date createdOn;
    private Date updatedOn;
    private Long agencyId;
    private String agencyName;
}
