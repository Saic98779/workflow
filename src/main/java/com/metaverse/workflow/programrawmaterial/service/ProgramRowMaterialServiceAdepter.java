package com.metaverse.workflow.programrawmaterial.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.ProgramRawMaterial;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.service.ProgramResponseMapper;
import com.metaverse.workflow.programattendance.service.ProgramAttendanceResponseMapper;
import com.metaverse.workflow.programrawmaterial.repository.ProgramRawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProgramRowMaterialServiceAdepter implements ProgramRawMaterialService {

    @Autowired
    ProgramRepository programRepository;

    @Autowired
    ProgramRawMaterialRepository programRawMaterialRepository;
    @Autowired
    ParticipantRepository participantRepository;


    @Override
    public WorkflowResponse rawMaterialByProgramId(Long programId, int page, int size) {
        Optional<Program> program = programRepository.findById(programId);
        if (!program.isPresent())
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

        ProgramRawMaterialResponse response = populateParticipantRawMaterial(
                programId,
                participants,
                dateSet.size());

        List<ProgramRawMaterial> list = programRawMaterialRepository.findByProgramRawMaterials(programId);
        if (list == null || list.isEmpty()) {
            return WorkflowResponse.builder().status(200).message("Success").data(response)
                    .totalElements(totalElements).totalPages(totalPages).build();
        } else {
            response = updateParticipantRawMaterials(list, response);
            return WorkflowResponse.builder().status(200).message("Success").data(response)
                    .totalElements(totalElements).totalPages(totalPages).build();
        }
    }


    private ProgramRawMaterialResponse updateParticipantRawMaterials(List<ProgramRawMaterial> list, ProgramRawMaterialResponse response) {
        Map<Long, String> existingDetailsMap = list.stream().collect(Collectors.toMap(details -> details.getProgramAttendanceId().getParticipantId(), details -> details.getProgramRawMaterialUsedData()));
        for(ProgramRawMaterialResponse.ParticipantRawMaterial rawMaterial : response.getParticipantRawMaterialList())
        {
            String rawMaterials = existingDetailsMap.get(rawMaterial.getParticipantId());
            if(rawMaterials != null) {
                Character [] charArray = new Character[rawMaterials.length()];
                for(int i=0;i< rawMaterials.length();i++)
                {
                    charArray[i]=rawMaterials.charAt(i);
                }
                rawMaterial.setRawMaterialData(charArray);
            }
        }
        return response;
    }


    @Override
    public WorkflowResponse updateProgramRawMaterial(ProgramRawMaterialRequest request) {
        List<ProgramRawMaterial> rawMaterialList =ProgramRawMaterialRequestMapper.map(request);
        List<ProgramRawMaterial> response = programRawMaterialRepository.saveAll(rawMaterialList);
        return WorkflowResponse.builder().status(200).message("Success").data(ProgramRawMaterialResponseMapper.map(response)).build();

    }





    private ProgramRawMaterialResponse populateParticipantRawMaterial(Long programId, List<Participant> participants, Integer days) {
        List<ProgramRawMaterialResponse.ParticipantRawMaterial> list = participants.stream().map(participant ->
                ProgramRawMaterialResponse.ParticipantRawMaterial.builder()
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
                        .rawMaterialData(populateRawmaterialdata(days))
                        .build()).collect(Collectors.toList());
        return ProgramRawMaterialResponse.builder().programId(programId).participantRawMaterialList(list).build();
    }


    private Character[] populateRawmaterialdata(Integer days)
    {
        Character[] charArray = new Character[days];
        for (int i = 0; i < days; i++) {
            charArray[i] = 'A';
        }
        return charArray;
    }

}
