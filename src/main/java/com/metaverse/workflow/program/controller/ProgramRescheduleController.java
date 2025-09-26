package com.metaverse.workflow.program.controller;

import com.metaverse.workflow.common.util.ApplicationAPIResponse;
import com.metaverse.workflow.program.service.ProgramRescheduleResponse;
import com.metaverse.workflow.program.service.ProgramRescheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/program-reschedule-data")
@RequiredArgsConstructor
public class ProgramRescheduleController {

    private final ProgramRescheduleService programRescheduleService;

    @GetMapping("/{programId}")
    public ResponseEntity<ApplicationAPIResponse<List<ProgramRescheduleResponse>>> getReschedules(@PathVariable Long programId) {
        List<ProgramRescheduleResponse> response = programRescheduleService.getReschedulesByProgramId(programId);

        ApplicationAPIResponse<List<ProgramRescheduleResponse>> applicationAPIResponse = ApplicationAPIResponse.<List<ProgramRescheduleResponse>>builder()
                .status(200)
                .message("Success")
                .data(response)
                .build();

        return ResponseEntity.ok(applicationAPIResponse);
    }
}

