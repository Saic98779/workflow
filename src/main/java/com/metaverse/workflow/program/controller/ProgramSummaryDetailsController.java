package com.metaverse.workflow.program.controller;

import com.metaverse.workflow.program.service.ProgramSummaryDetailsDto;
import com.metaverse.workflow.program.service.ProgramSummaryDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/program-summary")
@RequiredArgsConstructor
public class ProgramSummaryDetailsController {

    private final ProgramSummaryDetailsService service;

    @PostMapping
    public ProgramSummaryDetailsDto createOrUpdate(@RequestBody ProgramSummaryDetailsDto dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{programId}")
    public ProgramSummaryDetailsDto getByProgramId(@PathVariable Long programId) {
        return service.getByProgramId(programId);
    }

    @DeleteMapping("/{programId}")
    public String delete(@PathVariable Long programId) {
        service.deleteByProgramId(programId);
        return "Deleted successfully";
    }
}
