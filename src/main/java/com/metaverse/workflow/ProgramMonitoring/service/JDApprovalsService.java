package com.metaverse.workflow.ProgramMonitoring.service;

import com.metaverse.workflow.ProgramMonitoring.dto.JDApprovalsDto;
import com.metaverse.workflow.ProgramMonitoring.repository.JDApprovalsRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.JDApprovals;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.program.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JDApprovalsService {

    private final JDApprovalsRepository repository;

    private final ProgramRepository programRepository;


    public WorkflowResponse createApproval(JDApprovalsDto approval) {

        Optional<Program> byId = programRepository.findById(approval.getProgramId());

        if(byId.isPresent()){
            JDApprovals saved = repository.save(toEntity(approval));
            return WorkflowResponse.builder()
                    .status(200).message("JD Approval created successfully")
                    .data(toDto(saved)).build();
        }
        return WorkflowResponse.builder()
                .status(200).message("Program id not fount")
                .build();
    }

    public WorkflowResponse getAllApprovals() {
        List<JDApprovalsDto> list = repository.findAll().stream().map(jd -> toDto(jd)).toList();
        return WorkflowResponse.builder()
                .status(200)
                .message("Fetched all JD approvals")
                .data(list)
                .build();
    }

    public WorkflowResponse getApprovalById(Long id) {
        Optional<JDApprovals> approval = repository.findById(id);
        if (approval.isEmpty()) {
            return WorkflowResponse.builder()
                    .status(404)
                    .message("JD Approval not found with ID: " + id)
                    .data(null)
                    .build();
        }
        return WorkflowResponse.builder()
                .status(200)
                .message("JD Approval found")
                .data(toDto(approval.get()))
                .build();
    }

    public WorkflowResponse updateApproval(Long id, JDApprovalsDto updatedApproval) {
        return repository.findById(id).map(existing -> {
            existing.setProgramId(updatedApproval.getProgramId());
            existing.setStatus(updatedApproval.getStatus());
            existing.setRemarks(updatedApproval.getRemarks());
            JDApprovals saved = repository.save(existing);
            return WorkflowResponse.builder()
                    .status(200)
                    .message("JD Approval updated successfully")
                    .data(toDto(saved))
                    .build();
        }).orElseGet(() ->
                WorkflowResponse.builder()
                        .status(404)
                        .message("JD Approval not found with ID: " + id)
                        .build()
        );
    }

    public WorkflowResponse deleteApproval(Long id) {
        if (!repository.existsById(id)) {
            return WorkflowResponse.builder()
                    .status(404)
                    .message("JD Approval not found with ID: " + id)
                    .build();
        }
        repository.deleteById(id);
        return WorkflowResponse.builder()
                .status(200)
                .message("JD Approval deleted successfully")
                .build();
    }

    public JDApprovalsDto toDto(JDApprovals entity) {
        JDApprovalsDto dto = new JDApprovalsDto();
        dto.setJdApprovalsId(entity.getJdApprovalsId());
        dto.setProgramId(entity.getProgramId());
        dto.setStatus(entity.getStatus());
        dto.setRemarks(entity.getRemarks());
        return dto;
    }

    public JDApprovals toEntity(JDApprovalsDto dto) {
        JDApprovals entity = new JDApprovals();
        entity.setJdApprovalsId(dto.getJdApprovalsId());
        entity.setProgramId(dto.getProgramId());
        entity.setStatus(dto.getStatus());
        entity.setRemarks(dto.getRemarks());
        return entity;
    }
}