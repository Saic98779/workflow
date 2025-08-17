package com.metaverse.workflow.nontrainingactivity.service;

import com.metaverse.workflow.activity.repository.ActivityRepository;
import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Activity;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.NonTrainingExpenditure;
import com.metaverse.workflow.nontrainingactivity.repository.NonTrainingExpenditureRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NonTrainingExpenditureService {

    private final NonTrainingExpenditureRepo repository;
    private final AgencyRepository agencyRepository;
    private final ActivityRepository activityRepository;

    public WorkflowResponse create(NonTrainingExpenditureDTO dto) {
        Agency agency = agencyRepository.findById(dto.getAgencyId())
                .orElseThrow(() -> new RuntimeException("Agency not found"));
        Activity activity = activityRepository.findById(dto.getActivityId())
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        NonTrainingExpenditure entity = NonTrainingExpenditureMapper.toEntity(dto, agency, activity);
        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(NonTrainingExpenditureMapper.toDTO(repository.save(entity))).build();
    }

    public List<NonTrainingExpenditureDTO> getAll() {
        return repository.findAll().stream()
                .map(NonTrainingExpenditureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public NonTrainingExpenditureDTO getById(Long id) {
        return repository.findById(id)
                .map(NonTrainingExpenditureMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Expenditure not found"));
    }


    public NonTrainingExpenditureDTO update(Long id, NonTrainingExpenditureDTO dto) {
        NonTrainingExpenditure existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expenditure not found"));

        Agency agency = agencyRepository.findById(dto.getAgencyId())
                .orElseThrow(() -> new RuntimeException("Agency not found"));
        Activity activity = activityRepository.findById(dto.getActivityId())
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        NonTrainingExpenditure updated = NonTrainingExpenditureMapper.toEntity(dto, agency, activity);
        updated.setId(existing.getId());
        return NonTrainingExpenditureMapper.toDTO(repository.save(updated));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

