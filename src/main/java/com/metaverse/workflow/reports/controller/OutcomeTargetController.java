package com.metaverse.workflow.reports.controller;

import com.metaverse.workflow.reports.dto.OutcomeTargetDTO;
import com.metaverse.workflow.reports.dto.OutcomeTargetResponse;
import com.metaverse.workflow.reports.service.OutcomeTargetService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/outcome-targets")
@RequiredArgsConstructor
public class OutcomeTargetController {

    private final OutcomeTargetService outcomeTargetService;

    @GetMapping
    public ResponseEntity<?> getTargetsByYear(@RequestParam String year,
                                              @RequestParam Long agencyId) {

        List<OutcomeTargetDTO> targets =
                outcomeTargetService.getTargetsByYear(year, agencyId);

        // Skip recalculation for cumulative API
        if (!year.equals("-2")) {

            Map<String, int[]> outcomeTotals = new HashMap<>();

            for (OutcomeTargetDTO dto : targets) {

                outcomeTotals.computeIfAbsent(
                        dto.getOutcomeName(),
                        k -> new int[2]
                );

                outcomeTotals.get(dto.getOutcomeName())[0]
                        += dto.getTotalTarget();

                outcomeTotals.get(dto.getOutcomeName())[1]
                        += dto.getTotalAchieved();
            }

            for (OutcomeTargetDTO dto : targets) {

                int[] arr = outcomeTotals.get(dto.getOutcomeName());

                dto.setGrandTotalTarget(arr[0]);
                dto.setGrandTotalAchieved(arr[1]);
            }
        }

        return ResponseEntity.ok(targets);
    }
}
