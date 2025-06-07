package com.metaverse.workflow.programrawmaterial.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.programattendance.service.ProgramAttendanceRequest;
import com.metaverse.workflow.programrawmaterial.service.ProgramRawMaterialRequest;
import com.metaverse.workflow.programrawmaterial.service.ProgramRawMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ProgramRawMaterialController {


    @Autowired
    ProgramRawMaterialService programRawMaterialService;

    @GetMapping(value = "/program/rawmaterial/{programId}")
    public ResponseEntity<WorkflowResponse> rawMaterialByProgramId(@PathVariable("programId") Long programId,
                                                                   @RequestParam(value = "page", required = false) Integer page,
                                                                   @RequestParam(value = "size", required = false) Integer size) {
        log.info("Program RawMaterial controller, programId : {}", programId);
        WorkflowResponse response = programRawMaterialService.rawMaterialByProgramId(programId,page != null ?page :0, size != null ?size :0);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/program/rawmaterial", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<WorkflowResponse> saveProgramRawMaterial(@RequestBody ProgramRawMaterialRequest request) {
        log.info("Program Raw Material controller, programId : {}", request.getProgramId());
        WorkflowResponse response = programRawMaterialService.updateProgramRawMaterial(request);
        return ResponseEntity.ok(response);
    }
}
