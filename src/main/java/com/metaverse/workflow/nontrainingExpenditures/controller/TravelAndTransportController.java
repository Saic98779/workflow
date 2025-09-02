package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.metaverse.workflow.nontrainingExpenditures.Dto.TravelAndTransportDto;
import com.metaverse.workflow.nontrainingExpenditures.service.TravelAndTransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travel")
@RequiredArgsConstructor
public class TravelAndTransportController {

    private final TravelAndTransportService travelService;

    @PostMapping(path = "save")
    public TravelAndTransportDto saveTravel(@RequestBody TravelAndTransportDto dto) {
        return travelService.saveTravel(dto);
    }


    @GetMapping("/{subActivityId}")
    public List<TravelAndTransportDto> getByActivityId(@PathVariable Long subActivityId) {
        return travelService.getBySubActivityId(subActivityId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTravel(@PathVariable("id") Long id) {
        travelService.deleteById(id);
        return ResponseEntity.ok("TravelAndTransport with id " + id + " deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelAndTransportDto> updateTravel(
            @PathVariable("id") Long id,
            @RequestBody TravelAndTransportDto dto) {
        TravelAndTransportDto updated = travelService.updateTravel(id, dto);
        return ResponseEntity.ok(updated);
    }
}
