package com.metaverse.workflow.sector.service;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class SectorResponse {

    private Integer sectorId;
    private String sectorName;
    private List<String> organizations;


}
