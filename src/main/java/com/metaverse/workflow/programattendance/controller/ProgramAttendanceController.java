package com.metaverse.workflow.programattendance.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.program.controller.ProgramStatusController;
import com.metaverse.workflow.programattendance.service.ParticipantAttendanceRequest;
import com.metaverse.workflow.programattendance.service.ProgramAttendanceRequest;
import com.metaverse.workflow.programattendance.service.ProgramAttendanceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ProgramAttendanceController {

    private final ProgramAttendanceService programAttendanceService;
    private final ActivityLogService logService;
    private static final Logger log = LogManager.getLogger(ProgramAttendanceController.class);

    @GetMapping(value = "/program/attendence/{programId}")
    public ResponseEntity<WorkflowResponse> attendenceByProgramId(@PathVariable("programId") Long programId,
                                                                  @RequestParam(value = "page", required = false) Integer page,
                                                                  @RequestParam(value = "size", required = false) Integer size) {
        WorkflowResponse response = programAttendanceService.attendanceByProgramId(programId, page != null ?page :0, size != null ?size :0);
        log.info("program attendance get by program id"+ programId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/program/attendence", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<WorkflowResponse> saveProgramAttendance(@RequestBody ProgramAttendanceRequest request, Principal principal,
                                                                  HttpServletRequest servletRequest) {
        WorkflowResponse response = programAttendanceService.updateProgramAttendance(request);
        logService.logs(principal.getName(), "SAVE",
                "Program attendance saved successfully | Program ID: " + request.getProgramId(),
                "ProgramAttendance",
                servletRequest.getRequestURI());
        log.info("program attendance save successfully | Program ID: " + request.getProgramId());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/participant/attendance", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<WorkflowResponse> saveParticipantAttendance(@RequestBody ParticipantAttendanceRequest request,Principal principal,
                                                                      HttpServletRequest servletRequest) {

        WorkflowResponse response = programAttendanceService.updateParticipantAttendance(request);
        logService.logs(principal.getName(), "SAVE",
                "Participant attendance saved successfully | Program ID: " + request.getProgramId() +
                        " | Participant ID: " + request.getParticipantId(),
                "ParticipantAttendance",
                servletRequest.getRequestURI());
        log.info("Participant attendance saved successfully");
        return ResponseEntity.ok(response);
    }

}
