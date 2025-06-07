package com.metaverse.workflow.agency.controller;

import com.metaverse.workflow.agency.service.AgencyResponse;
import com.metaverse.workflow.agency.service.AgencyResponseMapper;
import com.metaverse.workflow.agency.service.AgencyService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.location.service.LocationResponse;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.ProgramSession;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.participant.service.ParticipantResponse;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.service.ProgramResponse;
import com.metaverse.workflow.program.service.ProgramResponseMapper;
import com.metaverse.workflow.resouce.service.ResourceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AgencyController {

    @Autowired
    private AgencyService service;

    @Autowired
    private ProgramRepository programRepository;


    @GetMapping("/agency/get/{id}")
    public ResponseEntity<WorkflowResponse> getAgencyById(@PathVariable("id") Long id) {
        Agency agency = service.getAgencyById(id);
        AgencyResponse response = AgencyResponseMapper.map(agency);
        return ResponseEntity.ok(WorkflowResponse.builder().message("Success").status(200).data(response).build());
    }

    @GetMapping("/agency/locations/{id}")
    public ResponseEntity<WorkflowResponse> getLocationsByAgencyId(@PathVariable("id") Long id) {
        Agency agency = service.getAgencyById(id);
        List<LocationResponse> response = AgencyResponseMapper.mapLocations(agency.getLocations());
        return ResponseEntity.ok(WorkflowResponse.builder().message("Success").status(200).data(response).build());
    }

    @GetMapping("/agency/resources/{id}")
    public ResponseEntity<WorkflowResponse> getResourcesByAgencyId(@PathVariable("id") Long id) {
        Agency agency = service.getAgencyById(id);
        List<ResourceResponse> response = AgencyResponseMapper.mapResources(agency.getResources());
        return ResponseEntity.ok(WorkflowResponse.builder().message("Success").status(200).data(response).build());
    }

    @GetMapping("/agencies")
    public ResponseEntity<WorkflowResponse> getAgencies() {
        WorkflowResponse response = service.getAgencies();
        return ResponseEntity.ok(response);
    }

    private Sort getSortOrder(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "programId");
        }

        String[] sortParams = sort.split(",");
        String field = sortParams[0];
        Sort.Direction direction = Sort.Direction.DESC;

        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }

        return Sort.by(direction, field);
    }

    @GetMapping("/agency/programs/{id}")
    public ResponseEntity<WorkflowResponse> getProgramsByAgencyId(@PathVariable("id") Long id,
                                                                  @RequestParam(defaultValue = "0", required = false) int page,
                                                                  @RequestParam(defaultValue = "10", required = false) int size,
                                                                  @RequestParam(defaultValue = "programId,desc", required = false) String sort
    ) {

        Pageable pageable = PageRequest.of(page, size, getSortOrder(sort));
        Page<Program> programPage = (id == -1)
                ? programRepository.findAll(pageable)
                : programRepository.findByAgencyAgencyId(id, pageable);

        for (Program program : programPage) {
            List<ProgramSession> sessions = program.getProgramSessionList();
            if (sessions != null) {
                for (ProgramSession session : sessions) {
                    List<ProgramSessionFile> filteredFiles = session.getProgramSessionFileList()
                            .stream()
                            .filter(file -> "FILE".equalsIgnoreCase(file.getFileType()))
                            .collect(Collectors.toList());
                    session.setProgramSessionFileList(filteredFiles);
                }
            }
        }

        List<ProgramResponse> response = programPage.getContent().stream()
                .map(ProgramResponseMapper::mapProgram)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                WorkflowResponse.builder()
                        .status(200)
                        .message("Success")
                        .data(response)
                        .totalElements(programPage.getTotalElements())
                        .totalPages(programPage.getTotalPages())
                        .build()
        );
    }

    @GetMapping("/agency/participants/{id}")
    public ResponseEntity<WorkflowResponse> getParticipantsByAgencyId(@PathVariable("id") Long id) {
        Agency agency = service.getAgencyById(id);
        List<ParticipantResponse> response = AgencyResponseMapper.mapParticipants(agency.getProgramList());
        return ResponseEntity.ok(WorkflowResponse.builder().message("Success").status(200).data(response).build());
    }

    @GetMapping("/agency/locationdetails/{id}")
    public ResponseEntity<WorkflowResponse> getLocationDetailsByAgencyId(@PathVariable("id") Long id) {
        Agency agency = service.getAgencyById(id);
        List<LocationResponse> response = AgencyResponseMapper.mapLocationDetails(agency.getLocations());
        return ResponseEntity.ok(WorkflowResponse.builder().message("Success").status(200).data(response).build());
    }

    @GetMapping("/agency/programs/dropdown/{id}")
    public ResponseEntity<WorkflowResponse> getProgramsByAgencyId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getProgramByAgencyIdDropDown(id));
    }

}
