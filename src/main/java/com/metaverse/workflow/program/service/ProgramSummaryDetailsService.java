package com.metaverse.workflow.program.service;


import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.ProgramSummaryDetails;

import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.repository.ProgramSummaryDetailsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramSummaryDetailsService {

    private final ProgramSummaryDetailsRepo repository;
    private final ProgramRepository programRepository;

    public ProgramSummaryDetailsDto createOrUpdate(ProgramSummaryDetailsDto dto) {

        Program program = programRepository.findById(dto.getProgramId()).orElseThrow(() -> new RuntimeException("Program not found"));

        ProgramSummaryDetails entity = repository.findByProgramProgramId(dto.getProgramId())
                .map(existing -> {
                    existing.setExecutiveSummary(dto.getExecutiveSummary());
                    existing.setCollegeDetails(dto.getCollegeDetails());
                    return existing;
                })
                .orElse(ProgramSummaryDetailsMapper.toEntity(dto, program));

        return ProgramSummaryDetailsMapper.toDto(repository.save(entity));
    }

    public ProgramSummaryDetailsDto getByProgramId(Long programId) {
        return repository.findByProgramProgramId(programId)
                .map(ProgramSummaryDetailsMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Details not found"));
    }

    public void deleteByProgramId(Long programId) {
        ProgramSummaryDetails entity = repository.findByProgramProgramId(programId)
                .orElseThrow(() -> new RuntimeException("Details not found"));

        repository.delete(entity);
    }
}