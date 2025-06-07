package com.metaverse.workflow.sector.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Builder
@Getter
@Setter
public class SectorRequest {

    private String sectorName;
    private List<Long> organizationIds;
}
