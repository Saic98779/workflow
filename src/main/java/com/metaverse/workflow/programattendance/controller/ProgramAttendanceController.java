package com.metaverse.workflow.programattendance.controller;

import com.metaverse.workflow.common.logs.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.programattendance.service.ParticipantAttendanceRequest;
import com.metaverse.workflow.programattendance.service.ProgramAttendanceRequest;
import com.metaverse.workflow.programattendance.service.ProgramAttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ProgramAttendanceController {

    private final ProgramAttendanceService programAttendanceService;
    @Autowired
    private ActivityLogService logService;

    public ProgramAttendanceController(ProgramAttendanceService programAttendanceService) {
        this.programAttendanceService = programAttendanceService;
    }

    @GetMapping(value = "/program/attendence/{programId}")
    public ResponseEntity<WorkflowResponse> attendenceByProgramId(@PathVariable("programId") Long programId,
                                                                  @RequestParam(value = "page", required = false) Integer page,
                                                                  @RequestParam(value = "size", required = false) Integer size) {

        log.info("Program attendance controller, programId : {}", programId);
        WorkflowResponse response = programAttendanceService.attendanceByProgramId(programId, page != null ?page :0, size != null ?size :0);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/program/attendence", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<WorkflowResponse> saveProgramAttendance(@RequestBody ProgramAttendanceRequest request) {
        log.info("Program attendance controller, programId : {}", request.getProgramId());
        WorkflowResponse response = programAttendanceService.updateProgramAttendance(request);
        logService.logs("Save","Attendance","Attendance added for "+request.getProgramId() + " this program");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/participant/attendance", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<WorkflowResponse> saveParticipantAttendance(@RequestBody ParticipantAttendanceRequest request) {
        log.info("Participant attendance controller, programId: {}, participantId: {}", 
                request.getProgramId(), request.getParticipantId());
        WorkflowResponse response = programAttendanceService.updateParticipantAttendance(request);
        logService.logs("Save","Attendance","Attendance added for "+request.getProgramId() + " this program");
        return ResponseEntity.ok(response);
    }

}
