package com.metaverse.workflow.participant.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.ParticipantTemp;
import com.metaverse.workflow.participant.service.ParticipantMigrationService;
import com.metaverse.workflow.participant.service.ParticipantTempResponse;
import com.metaverse.workflow.participant.service.ParticipantTempUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/participants/temp")
@RequiredArgsConstructor
public class ParticipantMigrationController {

    private final ParticipantMigrationService migrationService;

    @PostMapping("/migrate")
    public Map<String, List<?>> migrateParticipants(@RequestBody List<Long> tempIds) {
        return migrationService.migrateParticipants(tempIds);
    }

    @GetMapping("/{programId}")
    public ResponseEntity<WorkflowResponse> getTempParticipantsByProgramId(
            @PathVariable("programId") Long programId,
            @RequestParam(value = "agencyId", required = false) Long agencyId,
            @RequestParam(value = "failed", required = false) Boolean failed) {

        WorkflowResponse response = migrationService.getTempProgramParticipants(programId, agencyId, failed);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParticipantTempResponse> updateParticipantTemp(
            @PathVariable("id") Long participantTempId,
            @RequestBody ParticipantTempUpdateRequest request) {

        ParticipantTempResponse updated = migrationService.updateParticipantTemp(participantTempId, request);
        return ResponseEntity.ok(updated);
    }

}

