package com.metaverse.workflow.sector.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Sector;
import com.metaverse.workflow.organization.service.OrganizationService;
import com.metaverse.workflow.sector.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SectorServiceAdepter implements SectorService {

    @Autowired
    SectorRepository sectorRepository;

    @Autowired
    OrganizationService organizationService;


    @Override
    public WorkflowResponse saveSector(SectorRequest request) {

        List<Organization> organizationList = request.getOrganizationIds().stream()
                .map(organizationId -> organizationService.getOrganizationById(organizationId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        Sector sector=SectorRequestMapper.map(request,organizationList);
       Sector saveSector =sectorRepository.save(sector);

        return WorkflowResponse.builder().message("sector saved successfully").status(200).data(saveSector).build();
    }

    @Override
    public WorkflowResponse getAllSectors() {

        List<Sector> sectorList=sectorRepository.findAll();
        if(sectorList.isEmpty())return WorkflowResponse.builder().status(400).message("sectors not found").build();
        List<SectorResponse> responses=sectorList.stream().map(SectorResponseMapper::map).collect(Collectors.toList());
        return WorkflowResponse.builder().message("Success").status(200).data(responses).build();
    }
}
