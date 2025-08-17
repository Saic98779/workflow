package com.metaverse.workflow.nontrainingactivity.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.nontrainingactivity.service.NonTrainingExpenditureDTO;
import com.metaverse.workflow.nontrainingactivity.service.NonTrainingExpenditureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/non-training-expenditures")
@RequiredArgsConstructor
public class NonTrainingExpenditureController {

    private final NonTrainingExpenditureService service;

    @PostMapping
    public WorkflowResponse create(@RequestBody NonTrainingExpenditureDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public WorkflowResponse getAll() {
        List<NonTrainingExpenditureDTO> expenditures = service.getAll();
        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(expenditures)
                .build();
    }

    @GetMapping("/{id}")
    public WorkflowResponse getById(@PathVariable Long id) {
        NonTrainingExpenditureDTO dto = service.getById(id);
        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(dto)
                .build();
    }

    @PutMapping("/{id}")
    public WorkflowResponse update(@PathVariable Long id, @RequestBody NonTrainingExpenditureDTO dto) {
        NonTrainingExpenditureDTO updated = service.update(id, dto);
        return WorkflowResponse.builder()
                .status(200)
                .message("Updated Successfully")
                .data(updated)
                .build();
    }

    @DeleteMapping("/{id}")
    public WorkflowResponse delete(@PathVariable Long id) {
        service.delete(id);
        return WorkflowResponse.builder()
                .status(200)
                .message("Deleted Successfully")
                .build();
    }
}

