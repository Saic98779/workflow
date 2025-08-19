package com.metaverse.workflow.nontrainingactivity.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingactivity.service.NonTrainingExpenditureDTO;
import com.metaverse.workflow.nontrainingactivity.service.NonTrainingExpenditureService;
import com.metaverse.workflow.nontrainingactivity.service.NonTrainingResourceDTO;
import com.metaverse.workflow.nontrainingactivity.service.NonTrainingResourceExpenditureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/non-training")
@RequiredArgsConstructor
public class NonTrainingExpenditureController extends RestControllerBase {

    private final NonTrainingExpenditureService service;

    @PostMapping("/expenditure")
    public ResponseEntity<?> create(@RequestBody NonTrainingExpenditureDTO dto) {
        try {
            WorkflowResponse response = service.create(dto);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return error(e);
        }
    }

    @GetMapping("/expenditure/all")
    public ResponseEntity<?> getAll() {
        List<NonTrainingExpenditureDTO> expenditures = service.getAll();
        return ResponseEntity.ok(
                WorkflowResponse.builder()
                        .status(200)
                        .message("Success")
                        .data(expenditures)
                        .build()
        );
    }

    @GetMapping("/expenditure/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            NonTrainingExpenditureDTO dto = service.getById(id);
            return ResponseEntity.ok(
                    WorkflowResponse.builder()
                            .status(200)
                            .message("Success")
                            .data(dto)
                            .build()
            );
        } catch (DataException e) {
            return error(e);
        }
    }

    @PutMapping("/expenditure/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody NonTrainingExpenditureDTO dto) {
        try {
            NonTrainingExpenditureDTO updated = service.update(id, dto);
            return ResponseEntity.ok(
                    WorkflowResponse.builder()
                            .status(200)
                            .message("Updated Successfully")
                            .data(updated)
                            .build()
            );
        } catch (DataException e) {
            return error(e);
        }
    }

    @DeleteMapping("/expenditure/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(
                    WorkflowResponse.builder()
                            .status(200)
                            .message("Deleted Successfully")
                            .build()
            );
        } catch (DataException e) {
            return error(e);
        }
    }

    @PostMapping("/resource")
    public ResponseEntity<?> saveResource(@RequestBody NonTrainingResourceDTO resourceDto) {
        return ResponseEntity.ok(service.saveResource(resourceDto));
    }

    @PutMapping("/resource/update/{id}")
    public ResponseEntity<?> updateResource(@PathVariable Long id,
                                            @RequestBody NonTrainingResourceDTO resourceDto) {
        return ResponseEntity.ok(service.updateResource(id, resourceDto));
    }

    @DeleteMapping("/resource/delete/{id}")
    public ResponseEntity<?> deleteResource(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.deleteResource(id));
        } catch (DataException e) {
            return error(e);
        }
    }

    @PostMapping("/resource/expenditure")
    public ResponseEntity<?> saveResourceExpenditure(@RequestBody NonTrainingResourceExpenditureDTO expenditureDto) {
        try {
            return ResponseEntity.ok(service.saveResourceExpenditure(expenditureDto));
        } catch (DataException e) {
            return error(e);
        }
    }

    @PutMapping("/resource/expenditure/update/{expenditureId}")
    public ResponseEntity<?> updateResourceExpenditure(@PathVariable Long expenditureId,
                                                       @RequestBody NonTrainingResourceExpenditureDTO expenditureDto) {
        try {
            return ResponseEntity.ok(service.updateResourceExpenditure(expenditureId, expenditureDto));
        } catch (DataException e) {
            return error(e);
        }
    }

    @DeleteMapping("/resource/expenditure/delete/{expenditureId}")
    public ResponseEntity<?> deleteResourceExpenditure(@PathVariable Long expenditureId) {
        try {
            return ResponseEntity.ok(service.deleteResourceExpenditure(expenditureId));
        } catch (DataException e) {
            return error(e);
        }
    }
}
