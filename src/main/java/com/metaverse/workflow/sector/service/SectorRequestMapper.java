package com.metaverse.workflow.sector.service;

import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Sector;

import java.util.List;

public class SectorRequestMapper {

    public static Sector map(SectorRequest request, List<Organization> organizationsList)
    {
        return Sector.builder()
                .sectorName(request.getSectorName())
                .organizations(organizationsList)
                .build();
    }
}
