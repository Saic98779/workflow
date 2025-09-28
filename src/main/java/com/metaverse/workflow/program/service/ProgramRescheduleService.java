package com.metaverse.workflow.program.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.ProgramReschedule;
import com.metaverse.workflow.program.repository.ProgramRescheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramRescheduleService {

    private final ProgramRescheduleRepository programRescheduleRepository;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public List<ProgramRescheduleResponse> getReschedulesByProgramId(Long programId) {
        List<ProgramReschedule> reschedules = programRescheduleRepository.findByProgram_ProgramId(programId);

        return reschedules.stream()
                .map(r -> ProgramRescheduleResponse.builder()
                        .rescheduleId(r.getId())
                        .oldStartDate(r.getOldStartDate() != null ? dateFormat.format(r.getOldStartDate()) : null)
                        .newStartDate(r.getNewStartDate() != null ? dateFormat.format(r.getNewStartDate()) : null)
                        .createdTimestamp(r.getCreatedTimestamp() != null ? r.getCreatedTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null)
                        .programTitle(r.getProgram() != null ? r.getProgram().getProgramTitle() : null)
                        .build())
                .collect(Collectors.toList());
    }

    public List<ProgramListDto> getRescheduleProgramList() {
        return programRescheduleRepository.findAll()
                .stream()
                .map(reSche -> new ProgramListDto(reSche.getProgram().getProgramId(),reSche.getProgram().getProgramTitle()))
                .collect(Collectors.toList());
    }
}

