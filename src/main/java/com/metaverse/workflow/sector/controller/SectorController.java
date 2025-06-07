package com.metaverse.workflow.sector.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.sector.service.SectorRequest;
import com.metaverse.workflow.sector.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SectorController {

    @Autowired
    SectorService sectorService;

    @PostMapping("/save/sector")
    public ResponseEntity<WorkflowResponse> saveSector(@RequestBody SectorRequest request) {
        WorkflowResponse response = sectorService.saveSector(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getallsectors")
    public ResponseEntity<WorkflowResponse> getAllSectors() {
        WorkflowResponse response = sectorService.getAllSectors();
        return ResponseEntity.ok(response);
    }
}
