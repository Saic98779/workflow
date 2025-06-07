package com.metaverse.workflow.sector.service;

import com.metaverse.workflow.model.Sector;

import java.util.stream.Collectors;

public class SectorResponseMapper {

    public static SectorResponse map(Sector sector)
    {
        return SectorResponse.builder()
                .sectorId(sector.getSectorId())
                .sectorName(sector.getSectorName())
                .organizations(sector.getOrganizations().stream().map(organization-> organization.getOrganizationName()).collect(Collectors.toList())).build();
    }

}
