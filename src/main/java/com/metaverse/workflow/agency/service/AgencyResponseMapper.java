package com.metaverse.workflow.agency.service;

import com.metaverse.workflow.location.service.LocationResponse;
import com.metaverse.workflow.location.service.LocationResponseMapper;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.participant.service.ParticipantResponse;
import com.metaverse.workflow.participant.service.ParticipantResponseMapper;
import com.metaverse.workflow.program.service.ProgramResponse;
import com.metaverse.workflow.program.service.ProgramResponseMapper;
import com.metaverse.workflow.resouce.service.ResourceResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AgencyResponseMapper {

    public static AgencyResponse map(Agency agency) {
        AgencyResponse response = AgencyResponse.builder()
                .agencyId(agency.getAgencyId())
                .agencyName(agency.getAgencyName())
                .email(agency.getEmail())
                .address(agency.getAddress())
                .mobileNo(agency.getMobileNo())
                .createdOn(agency.getCreatedOn())
                .updatedOn(agency.getUpdatedOn())
                .build();
        return response;
    }

    public static List<ResourceResponse> mapResources(List<Resource> resourceList) {
        return resourceList != null ? resourceList.stream().map(resource ->
                ResourceResponse.builder()
                        .resourceId(resource.getResourceId())
                        .name(resource.getName())
                        .gender(resource.getGender())
                        .mobileNo(resource.getMobileNo())
                        .organizationName(resource.getOrganizationName())
                        .email(resource.getEmail())
                        .designation(resource.getDesignation())
                        .qualification(resource.getQualification())
                        .specialization(resource.getSpecialization())
                        .briefDescription(resource.getBriefDescription())
                        .build()).collect(Collectors.toList()) : null;

    }

    public static List<LocationResponse> mapLocations(List<Location> locationList) {
        return locationList != null ? locationList.stream().map(location ->
                LocationResponse.builder()
                        .locationId(location.getLocationId())
                        .locationName(location.getLocationName())
                        .build()).collect(Collectors.toList()) : null;
    }

    public static List<ProgramResponse> mapPrograms(List<Program> programList) {
        return programList != null ? programList.stream().map(program ->
                        ProgramResponseMapper.mapProgram(program)
                ).collect(Collectors.toList()) : null;
    }

    public static List<ParticipantResponse> mapParticipants(List<Program> programList) {
        List<Participant> participantList = programList != null ? programList.stream().flatMap(program ->
                program.getParticipants().stream()).collect(Collectors.toList()) : null;
        return participantList != null ? participantList.stream().map(participant ->
                ParticipantResponseMapper.map(participant))
                .collect(Collectors.toList()) : null;
    }

    public static List<LocationResponse> mapLocationDetails(List<Location> locationList) {
        return locationList != null ? locationList.stream().map(location ->
                LocationResponseMapper.map(location)).collect(Collectors.toList()) : null;
    }
}
