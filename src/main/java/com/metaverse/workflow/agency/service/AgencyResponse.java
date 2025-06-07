package com.metaverse.workflow.agency.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.metaverse.workflow.location.service.LocationResponse;
import com.metaverse.workflow.program.service.ProgramResponse;
import com.metaverse.workflow.resouce.service.ResourceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgencyResponse {
    private Long agencyId;
    private String agencyName;
    private Long mobileNo;
    private String email;
    private String address;
    private List<LocationResponse> locations;
    private List<ProgramResponse> programs;
    private List<ResourceResponse> resources;
    private Date createdOn;
    private Date updatedOn;
}
