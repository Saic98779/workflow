package com.metaverse.workflow.participant.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.participant.service.ParticipantMigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/participants")
@RequiredArgsConstructor
public class ParticipantMigrationController {

    private final ParticipantMigrationService migrationService;

    @PostMapping("/migrate")
    public Map<String, List<?>> migrateParticipants(@RequestBody List<Long> tempIds) {
        return migrationService.migrateParticipants(tempIds);
    }

    @GetMapping("/program/participants/temp/{programId}")
    public ResponseEntity<WorkflowResponse> getTempParticipantsByProgramId(
            @PathVariable("programId") Long programId,
            @RequestParam(value = "agencyId", required = false) Long agencyId,
            @RequestParam(value = "failed", required = false) Boolean failed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        WorkflowResponse response = migrationService.getTempProgramParticipants(programId, agencyId, page, size, failed);
        return ResponseEntity.ok(response);
    }
}

