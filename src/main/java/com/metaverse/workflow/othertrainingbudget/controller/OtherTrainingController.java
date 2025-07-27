package com.metaverse.workflow.othertrainingbudget.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.othertrainingbudget.service.OtherTrainingBudgetDTO;
import com.metaverse.workflow.othertrainingbudget.service.OtherTrainingExpenditureDTO;
import com.metaverse.workflow.othertrainingbudget.service.OtherTrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/other-training")
@RequiredArgsConstructor
public class OtherTrainingController {

    private final OtherTrainingService trainingService;


    @PostMapping("/expenditure")
    public ResponseEntity<?> createExpenditure(@RequestBody OtherTrainingExpenditureDTO request) {
        try {
            WorkflowResponse response = trainingService.createExpenditure(request);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }


    @PutMapping("/expenditure/{id}")
    public ResponseEntity<?> updateExpenditure(@PathVariable Long id, @RequestBody OtherTrainingExpenditureDTO request) {
        try {
            WorkflowResponse response = trainingService.updateExpenditure(id, request);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }


    @DeleteMapping("/expenditure/{id}")
    public ResponseEntity<?> deleteExpenditure(@PathVariable Long id) {
        WorkflowResponse response = trainingService.deleteExpenditure(id);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/budget")
    public ResponseEntity<?> saveTrainingBudget(@RequestBody OtherTrainingBudgetDTO request) {
        try {
            WorkflowResponse response = trainingService.saveTrainingBudget(request);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }


    @PutMapping("/budget/{id}")
    public ResponseEntity<?> updateTrainingBudget(@PathVariable Long id, @RequestBody OtherTrainingBudgetDTO request) {
        try {
            WorkflowResponse response = trainingService.updateTrainingBudget(id, request);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }


    @GetMapping("/budget/{id}")
    public ResponseEntity<?> getBudgetWithExpenditures(@PathVariable Long id) {
        OtherTrainingBudgetDTO response = trainingService.getBudgetWithExpenditures(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/budget/agency/{agencyId}")
    public ResponseEntity<?> getBudgetsByAgencyId(@PathVariable Long agencyId) {
        WorkflowResponse response = trainingService.getBudgetsByAgencyId(agencyId);
        return ResponseEntity.ok(response);
    }
}

