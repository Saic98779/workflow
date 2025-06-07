package com.metaverse.workflow.reports.controller;

import com.metaverse.workflow.reports.dto.OutcomeTargetDTO;
import com.metaverse.workflow.reports.dto.OutcomeTargetResponse;
import com.metaverse.workflow.reports.service.OutcomeTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/outcome-targets")
@RequiredArgsConstructor
public class OutcomeTargetController {

    private final OutcomeTargetService outcomeTargetService;

    @GetMapping
    public ResponseEntity<OutcomeTargetResponse> getTargetsByYear(@RequestParam String year, @RequestParam Long agencyId) {
        OutcomeTargetResponse outcomeTargetResponse = new OutcomeTargetResponse();
        List<OutcomeTargetDTO> targets = outcomeTargetService.getTargetsByYear(year,agencyId);
        int grandTargetTotal = targets.stream()
                .mapToInt(OutcomeTargetDTO::getTotalTarget)
                .sum();

        int grandAchievedTotal = targets.stream()
                .mapToInt(OutcomeTargetDTO::getTotalAchieved)
                .sum();
        outcomeTargetResponse.setOutcomeTargetDTOList(targets);
        outcomeTargetResponse.setGrandTargetTotal(grandTargetTotal);
        outcomeTargetResponse.setGrandAchievedTotal(grandAchievedTotal);
        return ResponseEntity.ok(outcomeTargetResponse);
    }
}
