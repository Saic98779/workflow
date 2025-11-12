package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.expenditure.service.ExpenditureRemarksDTO;
import com.metaverse.workflow.nontraining.dto.NonTrainingSubActivityDto;
import com.metaverse.workflow.nontraining.service.NonTrainingActivityService;
import com.metaverse.workflow.nontrainingExpenditures.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/non-training")
@RequiredArgsConstructor
public class NonTrainingExpenditureController extends RestControllerBase {

    private final NonTrainingExpenditureService service;
    private final NonTrainingActivityService nonTrainingActivityService;
    private final ActivityLogService logService;

    @PostMapping(path = "/save",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(Principal principal, @RequestPart("dto") String dto, @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NonTrainingExpenditureDTO nonTrainingExpenditureDTO = objectMapper.readValue(dto, NonTrainingExpenditureDTO.class);
            WorkflowResponse response = service.create(nonTrainingExpenditureDTO,file);
            logService.logs(principal.getName(), "SAVE", "Non-Training Expenditure created successfully", "NonTrainingExpenditure", "/non-training/save");
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

    @PutMapping(path = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestPart String dto,Principal principal, @RequestPart(value = "files", required = false)MultipartFile file) {
        try { //NonTrainingExpenditureDTO
            ObjectMapper objectMapper = new ObjectMapper();
            NonTrainingExpenditureDTO nonTrainingExpenditureDTO =    objectMapper.readValue(dto,NonTrainingExpenditureDTO.class);
            NonTrainingExpenditureDTO updated = service.update(id, nonTrainingExpenditureDTO,file);
            logService.logs(principal.getName(), "UPDATE", "Non-Training Expenditure updated successfully | ID: " + id, "NonTrainingExpenditure", "/non-training/update/" + id);

            return ResponseEntity.ok(
                    WorkflowResponse.builder()
                            .status(200)
                            .message("Updated Successfully")
                            .data(updated)
                            .build()
            );
        } catch (DataException e) {
            return error(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,Principal principal) {
        try {
            service.delete(id);
            logService.logs(principal.getName(), "DELETE", "Non-Training Expenditure deleted successfully | ID: " + id, "NonTrainingExpenditure", "/non-training-expenditure/delete/" + id);
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
    public ResponseEntity<?> saveResource(@RequestBody NonTrainingResourceDTO resourceDto,Principal principal) {
        try {
            WorkflowResponse response = service.saveResource(resourceDto);
            logService.logs(principal.getName(), "SAVE", "Non-Training Resource created successfully", "NonTrainingResource", "/non-training/resource");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return error(e);
        }

    }

    @PutMapping("/resource/update/{id}")
    public ResponseEntity<?> updateResource(@PathVariable Long id,Principal principal,
                                            @RequestBody NonTrainingResourceDTO resourceDto) {
        WorkflowResponse response = service.updateResource(id, resourceDto);
        logService.logs(principal.getName(), "UPDATE", "Non-Training Resource updated successfully | ID: " + id, "NonTrainingResource", "/non-training/resource/update/" + id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/resource/delete/{id}")
    public ResponseEntity<?> deleteResource(@PathVariable Long id,Principal principal) {
        try {
            WorkflowResponse response = service.deleteResource(id);
            logService.logs(principal.getName(), "DELETE", "Non-Training Resource deleted successfully | ID: " + id, "NonTrainingResource", "/non-training/resource/delete/" + id);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return error(e);
        }
    }

    @PostMapping(path="/non-training/expenditure/resource",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveResourceExpenditure(@RequestPart String expenditureDto,@RequestPart(value = "file", required = false) MultipartFile file,Principal principal) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NonTrainingResourceExpenditureDTO resourceExpenditureDTO = objectMapper.readValue(expenditureDto, NonTrainingResourceExpenditureDTO.class);
            WorkflowResponse response = service.saveResourceExpenditure(resourceExpenditureDTO,file);
            logService.logs(principal.getName(), "SAVE",
                    "Non-Training Resource Expenditure created successfully",
                    "NonTrainingResourceExpenditure",
                    "/non-training/expenditure/resource");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return error(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping(path = "/resource-expenditure/update/{expenditureId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateResourceExpenditure(@PathVariable Long expenditureId, Principal principal,
                                                       @RequestPart String expenditureDto, @RequestPart(value ="file", required = false)MultipartFile file) {
        try { //NonTrainingResourceExpenditureDTO
            ObjectMapper objectMapper = new ObjectMapper();
            NonTrainingResourceExpenditureDTO dto =  objectMapper.readValue(expenditureDto,NonTrainingResourceExpenditureDTO.class);
            WorkflowResponse response = service.updateResourceExpenditure(expenditureId, dto,file);
            logService.logs(principal.getName(), "UPDATE",
                    "Non-Training Resource Expenditure updated successfully | ID: " + expenditureId,
                    "NonTrainingResourceExpenditure",
                    "/non-training/expenditure/resource/update/" + expenditureId);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder()
                            .message("FAILURE: Invalid JSON format — " + e.getOriginalMessage()).status(400).build());

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder()
                            .message("FAILURE: File operation failed — " + e.getMessage()).status(500).build());

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.builder()
                            .message("FAILURE: " + e.getMessage()).status(400).build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    WorkflowResponse.builder()
                            .message("FAILURE: Unexpected server error — " + e.getMessage()).status(500).build());
        }
    }

    @DeleteMapping("/resource/expenditure/delete/{expenditureId}")
    public ResponseEntity<?> deleteResourceExpenditure(@PathVariable Long expenditureId,Principal principal) {
        try {
            WorkflowResponse  response = service.deleteResourceExpenditure(expenditureId);
            logService.logs(principal.getName(), "DELETE",
                    "Non-Training Resource Expenditure deleted successfully | ID: " + expenditureId,
                    "NonTrainingResourceExpenditure",
                    "/non-training/expenditure/resource/delete/" + expenditureId);
            return ResponseEntity.ok(response);
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

    @GetMapping(path = "/getAll/subActivities/{activityId}")
    public ResponseEntity<?> getActivityWiseSubActivities(@PathVariable Long activityId) {
        try {
            return ResponseEntity.ok(nonTrainingActivityService.getAllSubActivitiesList(activityId));
        }catch (Exception e){
          return ResponseEntity.status(400).body(WorkflowResponse.builder().status(400).message("Activity Id not found").build());
        }
    }


    @PutMapping("/save/remarks")
    public ResponseEntity<?> addingRemarks(Principal principal,@RequestBody NonTrainingExpenditureRemarksDTO remarksDTO,
                                           @RequestParam(value = "status", required = false) BillRemarksStatus status,
                                           HttpServletRequest servletRequest) {
        try {
            logService.logs(principal.getName(), "UPDATE", "Adding/updating remarks for non expenditure with status: " + status, "Program Expenditure", servletRequest.getRequestURI());
            return  ResponseEntity.ok(service.addRemarkOrResponse(remarksDTO, status));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
