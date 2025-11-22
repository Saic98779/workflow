package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingConsumablesBulkDto;
import com.metaverse.workflow.nontrainingExpenditures.service.NonTrainingConsumablesBulkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

import static com.metaverse.workflow.common.util.RestControllerBase.error;

@RestController
@RequestMapping("/non-training")
@RequiredArgsConstructor
public class NonTrainingConsumablesBulkController {

    private final NonTrainingConsumablesBulkService service;
    private final ActivityLogService logService;

    @PostMapping(path = "/consumables/bulk/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(
            Principal principal,
            @RequestPart("NonTrainingConsumablesBulkDto") String NonTrainingConsumablesBulkDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NonTrainingConsumablesBulkDto bulkDto =
                    objectMapper.readValue(NonTrainingConsumablesBulkDto, NonTrainingConsumablesBulkDto.class);

            WorkflowResponse response = service.saveBulkConsumable(bulkDto,file);

            logService.logs(principal.getName(),
                    "SAVE", "Non-Training Consumables Bulk created successfully",
                    "NonTrainingConsumablesBulk", "/non-training/consumables/bulk/save"
            );
            return ResponseEntity.ok(response);

        } catch (DataException e) {
            return error(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping(path = "/consumables/bulk/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestPart("NonTrainingConsumablesBulkDto") String bulkDtoString,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Principal principal
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            NonTrainingConsumablesBulkDto bulkDto =
                    mapper.readValue(bulkDtoString, NonTrainingConsumablesBulkDto.class);

            NonTrainingConsumablesBulkDto updated =
                    service.updateBulkConsumable(id, bulkDto, file);

            logService.logs(principal.getName(),
                    "UPDATE",
                    "Non-Training Consumables Bulk updated successfully | ID: " + id,
                    "NonTrainingConsumablesBulk",
                    "/non-training/consumables/bulk/update/" + id
            );

            return ResponseEntity.ok(
                    WorkflowResponse.builder()
                            .status(200)
                            .message("Updated Successfully")
                            .data(updated)
                            .build()
            );

        } catch (DataException e) {
            return error(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/consumables/bulk/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Principal principal) {
        try {
            service.deleteBulkConsumable(id);
            logService.logs(principal.getName(),
                    "DELETE", "Non-Training Consumables Bulk deleted successfully | ID: " + id,
                    "NonTrainingConsumablesBulk", "/non-training/consumables/bulk/delete/" + id
            );
            return ResponseEntity.ok(WorkflowResponse.builder().status(200).message("Deleted Successfully").build());
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }

    }

    @GetMapping("/consumables/bulk/sub-activity/{subActivityId}")
    public ResponseEntity<?> getBySubActivity(@PathVariable Long subActivityId) {
        try {
            return ResponseEntity.ok(WorkflowResponse.builder()
                    .message("success").status(200)
                    .data(service.getBySubActivity(subActivityId))
                    .build());
        } catch (Exception ex) {
            return error(new DataException("Failed to fetch records", "FETCH_ERROR", 500));
        }
    }

    @GetMapping("/consumables/bulk/{id}")
    public ResponseEntity<?> getNonTrainingBulkById(@PathVariable Long id) {
        try {
            NonTrainingConsumablesBulkDto dto = service.getNonTrainingConsumablesBulk(id);
            return ResponseEntity.ok(dto);
        } catch (DataException ex) {
            return error(ex);   // from RestControllerBase
        }
    }



}
