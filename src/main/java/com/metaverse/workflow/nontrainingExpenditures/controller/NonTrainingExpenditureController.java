package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingExpenditureDTO;
import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingExpenditureService;
import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingResourceDTO;
import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingResourceExpenditureDTO;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/non-training")
@RequiredArgsConstructor
public class NonTrainingExpenditureController extends RestControllerBase {

    private final NonTrainingExpenditureService service;

    @PostMapping(path = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@RequestPart("dto") String dto,@RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NonTrainingExpenditureDTO nonTrainingExpenditureDTO = objectMapper.readValue(dto, NonTrainingExpenditureDTO.class);
            WorkflowResponse response = service.create(nonTrainingExpenditureDTO,file);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return error(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/all")
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

    @GetMapping("/{id}")
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

    @PutMapping("/update/{id}")
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

    @DeleteMapping("/delete/{id}")
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
        try {
            return ResponseEntity.ok(service.saveResource(resourceDto));
        } catch (DataException e) {
            return error(e);
        }

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

    @PostMapping(path="/non-training/expenditure/resource",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveResourceExpenditure(@RequestPart String expenditureDto,@RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NonTrainingResourceExpenditureDTO resourceExpenditureDTO = objectMapper.readValue(expenditureDto, NonTrainingResourceExpenditureDTO.class);
            return ResponseEntity.ok(service.saveResourceExpenditure(resourceExpenditureDTO,file));
        } catch (DataException e) {
            return error(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/resource-expenditure/update/{expenditureId}")
    public ResponseEntity<?> updateResourceExpenditure(@PathVariable Long expenditureId,
                                                       @RequestBody NonTrainingResourceExpenditureDTO expenditureDto) {
        try {
            return ResponseEntity.ok(service.updateResourceExpenditure(expenditureId, expenditureDto));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/resource/expenditure/delete/{expenditureId}")
    public ResponseEntity<?> deleteResourceExpenditure(@PathVariable Long expenditureId) {
        try {
            return ResponseEntity.ok(service.deleteResourceExpenditure(expenditureId));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
    @GetMapping("/resources/non-training-activity")
    public ResponseEntity<?> getResourceFroDropdown(@PathParam("nonTrainingActivityId") Long nonTrainingActivityId)
    {
        try {
            return ResponseEntity.ok(service.getResourceByNonTrainingSubActivity(nonTrainingActivityId));
        }catch (DataException e)
        {
            return RestControllerBase.error(e);
        }
    }
    @GetMapping("/all/resources")
    public ResponseEntity<?> getResourceByActivity(@PathParam("nonTrainingActivityId") Long nonTrainingActivityId)
    {
        try {
            return ResponseEntity.ok(service.getAllResourceByNonTrainingActivityId(nonTrainingActivityId));
        }catch (DataException e)
        {
            return RestControllerBase.error(e);
        }
    }
    @GetMapping("/all/expenditures")
    public ResponseEntity<?> getExpenditureByActivity(@PathParam("nonTrainingActivityId") Long nonTrainingActivityId)
    {
        try {
            return ResponseEntity.ok(service.getAllExpenditureByNonTrainingActivityId(nonTrainingActivityId));
        }catch (DataException e)
        {
            return RestControllerBase.error(e);
        }
    }
    @GetMapping("/all/resource/expenditures")
    public ResponseEntity<?> getResourceExpenditureByActivity(@PathParam("nonTrainingActivityId") Long nonTrainingActivityId)
    {
        try {
            return ResponseEntity.ok(service.getAllResourceExpenditureByNonTrainingActivityId(nonTrainingActivityId));
        }catch (DataException e)
        {
            return RestControllerBase.error(e);
        }
    }
}
