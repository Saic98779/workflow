package com.metaverse.workflow.programattendance.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.ProgramAttendance;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.programattendance.repository.ProgramAttendanceRepository;
import com.metaverse.workflow.programattendance.util.AttendanceUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Collections;

@Service
public class ProgramAttendanceServiceAdapter implements ProgramAttendanceService {

    private final ProgramRepository programRepository;
    private final ParticipantRepository participantRepository;
    private final ProgramAttendanceRepository programAttendanceRepository;

    public ProgramAttendanceServiceAdapter(
            ProgramRepository programRepository,
            ParticipantRepository participantRepository,
            ProgramAttendanceRepository programAttendanceRepository) {
        this.programRepository = programRepository;
        this.participantRepository = participantRepository;
        this.programAttendanceRepository = programAttendanceRepository;
    }

    @Override
    public WorkflowResponse attendanceByProgramId(Long programId, int page, int size) {
        Optional<Program> program = programRepository.findById(programId);
        if (program.isEmpty())
            return WorkflowResponse.builder().status(400).message("Invalid Program").build();

        if (program.get().getProgramSessionList() == null)
            return WorkflowResponse.builder().status(400).message("No sessions created to program").build();

        Set<String> dateSet = program.get().getProgramSessionList().stream()
                .map(session -> DateUtil.dateToString(session.getSessionDate(), "dd-MM-yyyy"))
                .collect(Collectors.toSet());

        List<Participant> participants;
        long totalElements;
        int totalPages;

        if (page == 0 && size == 0) {
            // Fetch all participants (no pagination)
            participants = participantRepository.findByPrograms_ProgramId(programId);
            totalElements = participants.size();
            totalPages = 1;
        } else {
            Pageable pageable = PageRequest.of(page, size);
            Page<Participant> pagedParticipants = participantRepository.findByPrograms_ProgramId(programId, pageable);
            participants = pagedParticipants.getContent();
            totalElements = pagedParticipants.getTotalElements();
            totalPages = pagedParticipants.getTotalPages();
        }

        ProgramAttendanceResponse response = populateParticipantAttendace(
                programId,
                participants,
                dateSet.size()
        );

        List<ProgramAttendance> attendanceList = programAttendanceRepository.findByProgramAttendances(programId);
        if (attendanceList != null && !attendanceList.isEmpty()) {
            response = updateParticipantAttendances(attendanceList, response,dateSet.size());
        }

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(response)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }


    @Override
    public WorkflowResponse updateProgramAttendance(ProgramAttendanceRequest request) {
        List<ProgramAttendance> attendanceList = ProgramAttendanceRequestMapper.map(request);
        List<ProgramAttendance> response = programAttendanceRepository.saveAll(attendanceList);
        return WorkflowResponse.builder().status(200).message("Success").data(ProgramAttendanceResponseMapper.map(response)).build();
    }

    @Override
    public WorkflowResponse updateParticipantAttendance(ParticipantAttendanceRequest request) {
        WorkflowResponse validationResponse = validateProgramAndParticipant(
                request.getProgramId(), 
                request.getParticipantId());
        if (validationResponse != null) {
            return validationResponse;
        }
        ProgramAttendance attendance = ProgramAttendanceRequestMapper.mapSingleParticipant(request);
        ProgramAttendance savedAttendance = programAttendanceRepository.save(attendance);

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(ProgramAttendanceResponseMapper.map(Collections.singletonList(savedAttendance)))
                .build();
    }

    private ProgramAttendanceResponse populateParticipantAttendace(Long programId, List<Participant> participants, Integer days) {
        List<ProgramAttendanceResponse.ParticipantAttendance> list = participants.stream().map(participant ->
                ProgramAttendanceResponse.ParticipantAttendance.builder()
                        .participantId(participant.getParticipantId())
                        .participantName(participant.getParticipantName())
                        .memberId(participant.getMemberId())
                        .SHGName(participant.getOrganization() != null
                                ? participant.getOrganization().getNameOfTheSHG()
                                : null)
                        .mobileNo(participant.getMobileNo())
                        .email(participant.getEmail())
                        .aadharNo(participant.getAadharNo())
                        .designation(participant.getDesignation())
                        .attendanceData(populateAttendaceData(days))
                        .build()).collect(Collectors.toList());
        return ProgramAttendanceResponse.builder().programId(programId).participantAttendanceList(list).build();
    }

   private Character[] populateAttendaceData(Integer days) {
        Character[] charArray = new Character[days];
        Character defaultChar = (days == 1) ? 'P' : 'A';
        Arrays.fill(charArray, defaultChar);
        return charArray;
    }

    private ProgramAttendanceResponse updateParticipantAttendances(List<ProgramAttendance> list, ProgramAttendanceResponse response) {
        Map<Long, String> existingDetailsMap = list.stream().collect(Collectors.toMap(details -> details.getProgramAttendanceId().getParticipantId(), details -> details.getProgramAttendanceData()));
        for (ProgramAttendanceResponse.ParticipantAttendance attendance : response.getParticipantAttendanceList()) {
            String attendances = existingDetailsMap.get(attendance.getParticipantId());
            if (attendances != null) {
                attendance.setAttendanceData(AttendanceUtil.stringToCharacterArray(attendances));
            }
        }
        return response;
    }
    private ProgramAttendanceResponse updateParticipantAttendances(
            List<ProgramAttendance> list,
            ProgramAttendanceResponse response, int totalDays) {
        Map<Long, Character[]> existingDetailsMap = list.stream()
                .collect(Collectors.toMap(
                        d -> d.getProgramAttendanceId().getParticipantId(),
                        d -> AttendanceUtil.stringToCharacterArray(
                                d.getProgramAttendanceData()
                        )
                ));
        for (ProgramAttendanceResponse.ParticipantAttendance attendance : response.getParticipantAttendanceList()) {
            Character[] dbAttendance = existingDetailsMap.get(attendance.getParticipantId());
            if (dbAttendance != null) {
                Character[] normalizedAttendance = new Character[totalDays];
                for (int i = 0; i < totalDays; i++) {
                    if (i < dbAttendance.length) {
                        normalizedAttendance[i] = dbAttendance[i];
                    } else {
                        normalizedAttendance[i] = 'A';
                    }
                }
                attendance.setAttendanceData(normalizedAttendance);
            }
        }
        return response;
    }


    /**
     * Validates that the program and participant exist and that the participant is enrolled in the program
     * 
     * @param programId ID of the program
     * @param participantId ID of the participant
     * @return WorkflowResponse with error details if validation fails, null if validation passes
     */
    private WorkflowResponse validateProgramAndParticipant(Long programId, Long participantId) {
        Optional<Program> program = programRepository.findById(programId);
        if (program.isEmpty()) {
            return WorkflowResponse.builder().status(400).message("Invalid Program").build();
        }

        Optional<Participant> participant = participantRepository.findById(participantId);
        if (participant.isEmpty()) {
            return WorkflowResponse.builder().status(400).message("Invalid Participant").build();
        }

        boolean isEnrolled = participant.get().getPrograms().stream()
                .anyMatch(p -> p.getProgramId().equals(programId));
        if (!isEnrolled) {
            return WorkflowResponse.builder().status(400).message("Participant not enrolled in this program").build();
        }
        return null;
    }

}
