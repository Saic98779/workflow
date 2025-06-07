package com.metaverse.workflow.program.controller;

import com.metaverse.workflow.common.constants.ProgramStatusConstants;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.service.ProgramResponse;
import com.metaverse.workflow.program.service.ProgramResponseMapper;
import com.metaverse.workflow.program.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/programs/status")
public class ProgramStatusController {

    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProgramService programService;


    @PostMapping("/{programId}")
    public WorkflowResponse updateProgramStatus(
            @PathVariable Long programId,
            @RequestParam String status) {

        Optional<Program> programOptional = programRepository.findById(programId);

        if (programOptional.isEmpty()) {
            return WorkflowResponse.builder().message("Program not found").status(HttpStatus.BAD_REQUEST.value()).data(programId).build();
        }
        Program program = programOptional.get();
        if (isValidStatus(status)) {
            return WorkflowResponse.builder().message("Invalid status value." + status).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).data(programId).build();
        }
        program.setStatus(status);
        programRepository.save(program);
        return WorkflowResponse.builder().message("Program status updated successfully to: " + status).status(HttpStatus.OK.value()).data(programId).build();
    }

    private boolean isValidStatus(String status) {
        return !status.equals(ProgramStatusConstants.PROGRAM_SCHEDULED) &&
                !status.equals(ProgramStatusConstants.SESSIONS_CREATED) &&
                !status.equals(ProgramStatusConstants.PARTICIPANTS_ADDED) &&
                !status.equals(ProgramStatusConstants.ATTENDANCE_MARKED) &&
                !status.equals(ProgramStatusConstants.PROGRAM_EXECUTION_UPDATED) &&
                !status.equals(ProgramStatusConstants.PROGRAM_EXECUTION) &&
        !status.equals(ProgramStatusConstants.PROGRAM_EXPENDITURE_UPDATED);
    }

    @GetMapping("/{agencyId}")
    public WorkflowResponse getProgramsByStatus(@PathVariable Long agencyId,
                                                @RequestParam String status) {
        List<Program> programs = new ArrayList<>();
        if (isValidStatus(status)) {
            return WorkflowResponse.builder().message("Invalid status value." + status).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).data(status).build();
        }
        else {
            if (ProgramStatusConstants.PROGRAM_EXECUTION.equalsIgnoreCase(status)) {
                List<String> statuses = Arrays.asList(
                        ProgramStatusConstants.SESSIONS_CREATED,
                        ProgramStatusConstants.PARTICIPANTS_ADDED,
                        ProgramStatusConstants.ATTENDANCE_MARKED
                );
                programs = programRepository.findByAgencyAgencyIdAndStatusIn(agencyId, statuses);
            }
            else {
               programs = programRepository.findByAgencyAgencyIdAndStatus(agencyId, status);
            }
            List<ProgramResponse> response = programs != null ? programs.stream().map(ProgramResponseMapper::map).collect(Collectors.toList()) : null;
            return WorkflowResponse.builder().message("Success").status(200).data(response).build();
        }
    }
    @GetMapping("/summary/{agencyId}")
    public WorkflowResponse getProgramsStatusSummery(@PathVariable Long agencyId) {
        return programService.getProgramStatusSummery(agencyId);
    }
}
