package com.metaverse.workflow.program.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.ProgramReschedule;
import com.metaverse.workflow.model.ProgramSession;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.repository.ProgramRescheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramRescheduleService {

    private final ProgramRescheduleRepository programRescheduleRepository;
    private final ProgramRepository programRepository;

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

    public WorkflowResponse getRescheduleProgramList(Long agencyId, int page, int size, String sort) {
        try {
            Pageable pageable = PageRequest.of(page, size, getSortOrder(sort));

            List<Long> rescheduledProgramIds = programRescheduleRepository.findAllProgramIds();

            Page<Program> allPrograms = (agencyId == -1)
                    ? programRepository.findByProgramIdIn(rescheduledProgramIds,pageable)
                    : programRepository.findByAgencyAgencyIdAndProgramIdIn(agencyId,rescheduledProgramIds,pageable);

//            List<Program> rescheduledPrograms = allPrograms.stream()
//                    .filter(program -> rescheduledProgramIds.contains(program.getProgramId()))
//                    .toList();

//            int totalElements = rescheduledPrograms.size();
//            int totalPages = (int) Math.ceil((double) totalElements / size);
//
//            int start = Math.min(page * size, totalElements);
//            int end = Math.min(start + size, totalElements);
//
            List<Program> pagedPrograms = allPrograms.getContent();

            pagedPrograms.forEach(program -> {
                if (program.getProgramSessionList() != null) {
                    program.getProgramSessionList().forEach(session -> {
                        if (session.getProgramSessionFileList() != null) {
                            session.setProgramSessionFileList(
                                    session.getProgramSessionFileList().stream()
                                            .filter(file -> "FILE".equalsIgnoreCase(file.getFileType()))
                                            .toList()
                            );
                        }
                    });
                }
            });

            List<ProgramResponse> response = pagedPrograms.stream()
                    .map(ProgramResponseMapper::mapProgram)
                    .toList();

            return WorkflowResponse.builder()
                    .status(200)
                    .message("Success")
                    .data(response)
                    .totalElements(allPrograms.getTotalElements())
                    .totalPages(allPrograms.getTotalPages())
                    .build();

        } catch (Exception e) {
            return WorkflowResponse.builder()
                    .status(500)
                    .message("Failed to fetch reschedule programs: " + e.getMessage())
                    .build();
        }
    }

    private Sort getSortOrder(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "programId");
        }

        String[] sortParams = sort.split(",");
        String field = sortParams[0];
        Sort.Direction direction = Sort.Direction.DESC;

        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }

        return Sort.by(direction, field);
    }
}

