package com.metaverse.workflow.participant.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.ActivityLog;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.ParticipantTemp;
import com.metaverse.workflow.participant.service.ParticipantMigrationService;
import com.metaverse.workflow.participant.service.ParticipantTempResponse;
import com.metaverse.workflow.participant.service.ParticipantTempUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/participants/temp")
@RequiredArgsConstructor
public class ParticipantMigrationController {

    private final ParticipantMigrationService migrationService;
    private final ActivityLogService logService;

    @PostMapping("/migrate")
    public Map<String, List<?>> migrateParticipants(@RequestBody List<Long> tempIds, Principal principal) {
        logService.logs(principal.getName(), "migrate","participants save in temp while import","participant","/participants/temp/migrate");
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
    public ResponseEntity<ParticipantTempResponse> updateParticipantTemp(Principal principal,
            @PathVariable("id") Long participantTempId,
            @RequestBody ParticipantTempUpdateRequest request) {

        ParticipantTempResponse updated = migrationService.updateParticipantTemp(participantTempId, request);
        logService.logs(principal.getName(), "update","participants update after temp import ","participant","/participants/temp/{id}");

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteParticipantTemp(@PathVariable Long id,Principal principal) {
        migrationService.deleteById(id);
        logService.logs(principal.getName(), "delete","participants delete after temp import ","participant","/participants/temp/{id}");
        return ResponseEntity.ok("ParticipantTemp with ID " + id + " deleted successfully");
    }
}

