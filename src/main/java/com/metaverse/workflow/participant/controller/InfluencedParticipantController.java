package com.metaverse.workflow.participant.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.organization.controller.OrganizationController;
import com.metaverse.workflow.participant.service.InfluencedParticipantDto;
import com.metaverse.workflow.participant.service.InfluencedParticipantService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/influenced-participants")
@RequiredArgsConstructor
public class InfluencedParticipantController {

    private final InfluencedParticipantService service;
    private final static Logger log =  LogManager.getLogger(InfluencedParticipantController.class);

    @PostMapping(path = "/save")
    public ResponseEntity<WorkflowResponse> createParticipant(@RequestBody InfluencedParticipantDto influencedDto) throws DataException {
        try {
            WorkflowResponse response = service.create(influencedDto);
            log.info("saved Influenced Participant Successfully" + response);
            return ResponseEntity.ok(response);
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
            WorkflowResponse update = service.update(influencedId, influencedDto);
            log.info("updated Influenced Participant Successfully" + update);
            return ResponseEntity.ok(update);
        } catch (DataException e) {
            return ResponseEntity.status(400).body(WorkflowResponse.builder().data(null).status(400).message(e.getMessage()).build());
        }
    }

    @DeleteMapping("/{influencedId}")
    public ResponseEntity<WorkflowResponse> deleteParticipant(@PathVariable Long influencedId) {
        try {
            WorkflowResponse delete = service.delete(influencedId);
            log.info("deleted Influenced Participant Successfully" + delete);
            return ResponseEntity.ok(delete);
        } catch (DataException e) {
            return ResponseEntity.status(400).body(WorkflowResponse.builder().data(null).status(400).message(e.getMessage()).build());
        }
    }
}

