package com.metaverse.workflow.program.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.ApplicationAPIResponse;
import com.metaverse.workflow.program.service.ProgramRescheduleResponse;
import com.metaverse.workflow.program.service.ProgramRescheduleService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/program-reschedule-data")
@RequiredArgsConstructor
public class ProgramRescheduleController {

    private final ProgramRescheduleService programRescheduleService;
    private static final Logger log = LogManager.getLogger(ProgramRescheduleController.class);
    @GetMapping("/{programId}")
    public ResponseEntity<ApplicationAPIResponse<List<ProgramRescheduleResponse>>> getReschedules(@PathVariable Long programId) {
        List<ProgramRescheduleResponse> response = programRescheduleService.getReschedulesByProgramId(programId);

        ApplicationAPIResponse<List<ProgramRescheduleResponse>> applicationAPIResponse = ApplicationAPIResponse.<List<ProgramRescheduleResponse>>builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();
        log.info("successfully get by program id {}", programId);
        return ResponseEntity.ok(applicationAPIResponse);
    }

    @GetMapping(path = "/programs/{agencyId}")
    public ResponseEntity<WorkflowResponse> getRescheduleProgramList(@PathVariable Long agencyId,
                                                                     @RequestParam(defaultValue = "0", required = false) int page,
                                                                     @RequestParam(defaultValue = "10", required = false) int size,
                                                                     @RequestParam(defaultValue = "programId,desc", required = false) String sort){
        try {
            WorkflowResponse response = programRescheduleService.getRescheduleProgramList(agencyId, page, size, sort);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(WorkflowResponse.builder()
                            .status(500)
                            .message("An error occurred while fetching programs : "+e.getMessage())
                            .build());
        }
    }
}

