package com.metaverse.workflow.agency.controller;

import com.metaverse.workflow.agency.service.AgencyResponse;
import com.metaverse.workflow.agency.service.AgencyResponseMapper;
import com.metaverse.workflow.agency.service.AgencyService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.location.service.LocationResponse;
import com.metaverse.workflow.model.*;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<WorkflowResponse> getResourcesByAgencyId(
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {

        Agency agency = service.getAgencyById(id);
        List<Resource> resources = agency.getResources();
        int start = Math.min(page * size, resources.size());
        int end = Math.min(start + size, resources.size());
        List<Resource> pagedResources = resources.subList(start, end);

        List<ResourceResponse> response = AgencyResponseMapper.mapResources(pagedResources);

        WorkflowResponse workflowResponse = WorkflowResponse.builder()
                .message("Success")
                .status(200)
                .data(response)
                .totalPages((int) Math.ceil((double) resources.size() / size))
                .totalElements(resources.size())
                .build();

        return ResponseEntity.ok(workflowResponse);
    }
    @GetMapping("/agency/resources/dropdown/{id}")
    public ResponseEntity<WorkflowResponse> getResourcesDropdownByAgencyId(@PathVariable("id") Long id) {
        Agency agency = service.getAgencyById(id);
        List<Resource> resources = agency.getResources();

        // Map only resourceId and resourceName
        List<Map<String, Object>> dropdownList = resources.stream()
                .map(resource -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("resourceId", resource.getResourceId());
                    map.put("name", resource.getName());
                    return map;
                })
                .collect(Collectors.toList());

        WorkflowResponse response = WorkflowResponse.builder()
                .message("Success")
                .status(200)
                .data(dropdownList)
                .totalElements(dropdownList.size())
                .build();

        return ResponseEntity.ok(response);
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
                                                                  @RequestParam(defaultValue = "programId,desc", required = false) String sort,
                                                                  @RequestParam(required = false) String startDate,
                                                                  @RequestParam(required = false) String endDate,
                                                                  @RequestParam(required = false) String districtName
    ) {

        Pageable pageable = PageRequest.of(page, size, getSortOrder(sort));
        Page<Program> programPage;
        if (id == -1) {
            if (startDate != null && endDate != null) {
                if(districtName == null)
                    programPage = programRepository.findAllByStartDateBetween(DateUtil.stringToDate(startDate, "dd-MM-yyyy"), DateUtil.stringToDate(endDate, "dd-MM-yyyy"), pageable);
                else
                    programPage = programRepository.findAllByStartDateBetweenAndLocationDistrict(DateUtil.stringToDate(startDate, "dd-MM-yyyy"), DateUtil.stringToDate(endDate, "dd-MM-yyyy"), pageable,districtName);

            } else {
                programPage = programRepository.findAll(pageable);
            }
        } else {
            if (startDate != null && endDate != null) {
                if(districtName == null)
                    programPage = programRepository.findByAgencyAgencyIdAndStartDateBetween(id, DateUtil.stringToDate(startDate, "dd-MM-yyyy"), DateUtil.stringToDate(endDate, "dd-MM-yyyy"), pageable);
                else
                    programPage = programRepository.findByAgencyAgencyIdAndStartDateBetweenAndLocationDistrict(id, DateUtil.stringToDate(startDate, "dd-MM-yyyy"), DateUtil.stringToDate(endDate, "dd-MM-yyyy"), pageable,districtName);

            } else {
                programPage = programRepository.findByAgencyAgencyId(id, pageable);
            }
        }

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
    public ResponseEntity<WorkflowResponse> getLocationDetailsByAgencyId(@PathVariable("id") Long id,
                                                                         @RequestParam(defaultValue = "0", required = false) int page,
                                                                         @RequestParam(defaultValue = "10", required = false) int size) {
        return ResponseEntity.ok(service.getAllLocationByAgencyId(id, page, size));
    }

    @GetMapping("/agency/programs/dropdown/{id}")
    public ResponseEntity<WorkflowResponse> getProgramsByAgencyId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getProgramByAgencyIdDropDown(id));
    }

    @GetMapping("/agency/programs/dropdown")
    public ResponseEntity<WorkflowResponse> getProgramsDistrictsAndAgency(@RequestParam Long id,
                                                                          @RequestParam(required = false) String district) {
        return ResponseEntity.ok(service.getProgramsDistrictsAndAgency(id, district));
    }

    @GetMapping("/agency/programs/by/status/{id}")
    public ResponseEntity<WorkflowResponse> getProgramsByStatusAgencyId(
            @PathVariable("id") Long id,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "programId,desc", required = false) String sort,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String districtName
    ) {

        Pageable pageable = PageRequest.of(page, size, getSortOrder(sort));

        Page<Program> programPage;

        Date from = DateUtil.covertStringToDate(fromDate);
        Date to = DateUtil.covertStringToDate(toDate);

        if (status == null || status.isEmpty()) {
            // No status → return all programs with optional date filter
            if (from != null && to != null) {
                if (id == -1) {
                    if(districtName == null)
                        programPage = programRepository.findAllByStartDateBetween(from, to, pageable);
                    else
                        programPage = programRepository.findAllByStartDateBetweenAndLocationDistrict(from, to, pageable,districtName);
                } else {
                    if(districtName == null)
                        programPage = programRepository.findByAgencyAgencyIdAndStartDateBetween(id, from, to, pageable);
                    else
                        programPage = programRepository.findByAgencyAgencyIdAndStartDateBetweenAndLocationDistrict(id, from, to, pageable,districtName);

                }
            } else {
                if (id == -1) {
                    programPage = programRepository.findAll(pageable);
                } else {
                    programPage = programRepository.findByAgencyAgencyId(id, pageable);
                }
            }
        } else {
            // Status is present → use your existing logic
            if(districtName == null)
                programPage = programRepository.findByAgencyAgencyStatusId(id, pageable, status, from, to);
            else
                programPage = programRepository.findByAgencyAgencyStatusIdAndLocationDistrict(id, pageable, status, from, to,districtName);
        }

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


    @GetMapping("/agency/programs/district/{district}")
    public ResponseEntity<WorkflowResponse> getProgramsByDistrict(
            @PathVariable String district,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "programId,desc") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, getSortOrder(sort));
        Page<Program> programPage = programRepository.getProgramsByDistrict(district, pageable);

        // Filter program session files
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

}
