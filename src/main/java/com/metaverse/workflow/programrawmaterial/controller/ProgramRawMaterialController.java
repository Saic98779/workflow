package com.metaverse.workflow.programrawmaterial.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.programattendance.service.ProgramAttendanceRequest;
import com.metaverse.workflow.programrawmaterial.service.ProgramRawMaterialRequest;
import com.metaverse.workflow.programrawmaterial.service.ProgramRawMaterialService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
public class ProgramRawMaterialController {

    @Autowired
    private ProgramRawMaterialService programRawMaterialService;
    @Autowired
    private ActivityLogService logService;

    @GetMapping(value = "/program/rawmaterial/{programId}")
    public ResponseEntity<WorkflowResponse> rawMaterialByProgramId(@PathVariable("programId") Long programId,
                                                                   @RequestParam(value = "page", required = false) Integer page,
                                                                   @RequestParam(value = "size", required = false) Integer size) {
        log.info("Program RawMaterial controller, programId : {}", programId);
        WorkflowResponse response = programRawMaterialService.rawMaterialByProgramId(programId,page != null ?page :0, size != null ?size :0);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/program/rawmaterial", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<WorkflowResponse> saveProgramRawMaterial(@RequestBody ProgramRawMaterialRequest request, Principal principal,
                                                                   HttpServletRequest servletRequest) {
        log.info("Program Raw Material controller, programId : {}", request.getProgramId());
        WorkflowResponse response = programRawMaterialService.updateProgramRawMaterial(request);
        logService.logs(principal.getName(), "SAVE",
                "Program raw material saved successfully | Program ID: " + request.getProgramId(),
                "ProgramRawMaterial",
                servletRequest.getRequestURI());
        return ResponseEntity.ok(response);
    }
}
