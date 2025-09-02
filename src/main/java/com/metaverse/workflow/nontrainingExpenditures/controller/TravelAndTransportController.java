package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.expenditure.service.BulkExpenditureRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.TravelAndTransportDto;
import com.metaverse.workflow.nontrainingExpenditures.service.TravelAndTransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/travel")
@RequiredArgsConstructor
public class TravelAndTransportController {

    private final TravelAndTransportService travelService;

    @PostMapping(path = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WorkflowResponse> saveTravel(
            @RequestPart("dto") String dtoString,
            @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        TravelAndTransportDto dto = objectMapper.readValue(dtoString, TravelAndTransportDto.class);

        return ResponseEntity.ok(travelService.saveTravel(dto, file));
    }


    @GetMapping("/{subActivityId}")
    public ResponseEntity<?> getByActivityId(@PathVariable Long subActivityId) {
        if(subActivityId != null){
            return ResponseEntity.ok(WorkflowResponse.builder().data(travelService.getBySubActivityId(subActivityId)).message("SUCCESS").status(200).build());
        }
        return ResponseEntity.ok(WorkflowResponse.builder().message("FAILURE").status(200).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTravel(@PathVariable("id") Long id) {
        travelService.deleteById(id);
        return ResponseEntity.ok("TravelAndTransport with id " + id + " deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTravel(@PathVariable("id") Long id, @RequestBody TravelAndTransportDto dto) {

        try{
            TravelAndTransportDto updated = travelService.updateTravel(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.ofNullable(WorkflowResponse.builder().message("FAILURE").status(400).build());
        }

    }
}
