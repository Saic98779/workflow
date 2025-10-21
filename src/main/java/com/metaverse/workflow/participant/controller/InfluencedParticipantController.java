package com.metaverse.workflow.participant.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.participant.dto.InfluencedParticipantDto;
import com.metaverse.workflow.participant.service.InfluencedParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/influenced-participants")
@RequiredArgsConstructor
public class InfluencedParticipantController {

    private final InfluencedParticipantService service;

    @PostMapping(path = "/save")
    public ResponseEntity<WorkflowResponse> createParticipant(@RequestBody InfluencedParticipantDto influencedDto) throws DataException {
        try {
            return ResponseEntity.ok(service.create(influencedDto));
        } catch (DataException e) {
            return ResponseEntity.status(400).body(WorkflowResponse.builder().data(null).status(400).message(e.getMessage()).build());
        }
    }

    @GetMapping(path = "get-all")
    public ResponseEntity<WorkflowResponse> getAllParticipants() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{influencedId}")
    public ResponseEntity<WorkflowResponse> getParticipantById(@PathVariable Long influencedId) {
        try {
            return ResponseEntity.ok(service.getById(influencedId));
        } catch (DataException e) {
            return ResponseEntity.status(400).body(WorkflowResponse.builder().data(null).status(400).message(e.getMessage()).build());
        }
    }

    @PutMapping("/{influencedId}")
    public ResponseEntity<WorkflowResponse> updateParticipant(
            @PathVariable Long influencedId, @RequestBody InfluencedParticipantDto influencedDto) {
        try {
            return ResponseEntity.ok(service.update(influencedId, influencedDto));
        } catch (DataException e) {
            return ResponseEntity.status(400).body(WorkflowResponse.builder().data(null).status(400).message(e.getMessage()).build());
        }
    }

    @DeleteMapping("/{influencedId}")
    public ResponseEntity<WorkflowResponse> deleteParticipant(@PathVariable Long influencedId) {
        try {
            return ResponseEntity.ok(service.delete(influencedId));
        } catch (DataException e) {
            return ResponseEntity.status(400).body(WorkflowResponse.builder().data(null).status(400).message(e.getMessage()).build());
        }
    }
}

