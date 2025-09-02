package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.nontrainingExpenditures.Dto.NonTrainingExpenditureDto;
import com.metaverse.workflow.nontrainingExpenditures.service.ItInfraStructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/ItInfraStructure")
public class ItInfraStructureController {

    private final ItInfraStructureService service;


    @PostMapping(path = "/save")
    public ResponseEntity<?> create(@RequestBody NonTrainingExpenditureDto dto) {
        try {
            WorkflowResponse response = service.save(dto);
            return ResponseEntity.ok(response);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<NonTrainingExpenditureDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    @GetMapping("/subActivity/{subActivityId}")
    public ResponseEntity<List<NonTrainingExpenditureDto>> getBySubActivityId(@PathVariable Long subActivityId) {
        return ResponseEntity.ok(service.getBySubActivityId(subActivityId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Expenditure with id " + id + " deleted successfully");
    }

}
