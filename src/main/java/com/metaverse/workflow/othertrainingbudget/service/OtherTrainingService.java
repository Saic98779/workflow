package com.metaverse.workflow.othertrainingbudget.service;


import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.OtherTrainingBudget;
import com.metaverse.workflow.model.OtherTrainingExpenditure;
import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.othertrainingbudget.repository.OtherTrainingBudgetRepo;
import com.metaverse.workflow.othertrainingbudget.repository.OtherTrainingExpenditureRepo;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import com.metaverse.workflow.program.service.ProgramServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OtherTrainingService {

    private final OtherTrainingExpenditureRepo expenditureRepo;
    private final OtherTrainingBudgetRepo budgetRepo;
    private final AgencyRepository agencyRepo;
    private final ProgramServiceAdapter programServiceAdapter;
    private final ProgramSessionFileRepository programSessionFileRepository;
    private final OtherTrainingExpenditureRepo otherTrainingExpenditure;

    public WorkflowResponse createExpenditure(OtherTrainingExpenditureDTO request, MultipartFile file) throws DataException {
        List<ProgramSessionFile> sessionFiles = new ArrayList<>();
        OtherTrainingBudget trainingBudget = budgetRepo.findById(request.getBudgetId())
                .orElseThrow(() -> new DataException("Budget Not found for this Id: " + request.getBudgetId(), "BUDGET_HEAD_NOT_FOUND", 400));

        OtherTrainingExpenditure expenditure = OtherTrainingMapper.toEntity(request, trainingBudget);
        trainingBudget.addExpenditure(expenditure);
        budgetRepo.save(trainingBudget);
        if (file != null && !file.isEmpty()) {
            List<MultipartFile> files = Collections.singletonList(file);
            List<String> filePaths = programServiceAdapter.storageProgramFiles(files, request.getBudgetId(), "BudgetHeadFiles");
            if (!filePaths.isEmpty()) {
                expenditure.setBillPath(filePaths.get(0));
                otherTrainingExpenditure.save(expenditure);
            }
        }
        return WorkflowResponse.builder()
                .status(200)
                .message("Expenditure saved successfully")
                .data(OtherTrainingMapper.toDto(expenditure))
                .build();
    }


    public WorkflowResponse updateExpenditure(Long id, MultipartFile file, OtherTrainingExpenditureDTO dto) throws DataException {
        OtherTrainingExpenditure entity = expenditureRepo.findById(id)
                .orElseThrow(() -> new DataException("Expenditure Not found for this Id:" + id, "EXPENDITURE_NOT_FOUND", 400));
        dto.setDateOfExpenditure(DateUtil.dateToString(entity.getDateOfExpenditure(), "dd-MM-yyyy"));
        dto.setDetails(entity.getDetails());
        dto.setAmount(entity.getAmount());
        dto.setBillPath(entity.getBillPath());
        dto.setBillNo(entity.getBillNo());
        dto.setBillDate(DateUtil.dateToString(entity.getBillDate(), "dd-MM-yyyy"));
        expenditureRepo.save(entity);
        return WorkflowResponse.builder().status(200).message("Expenditure updated successfully").build();

    }

    public WorkflowResponse deleteExpenditure(Long id) {
        if (expenditureRepo.existsById(id)) {
            expenditureRepo.deleteById(id);
        } else WorkflowResponse.builder().status(200).message("Expenditure not found with this id :" + id).build();
        return WorkflowResponse.builder().status(200).message("Expenditure Deleted successfully").build();
    }

    public WorkflowResponse saveTrainingBudget(OtherTrainingBudgetDTO dto) throws DataException {
        System.out.println("start");
        Agency agency = agencyRepo.findById(dto.getAgencyId())
                .orElseThrow(() -> new DataException("Agency not found: " + dto.getAgencyId(), "AGENCY_NOT_FOUND", 400));

        OtherTrainingBudget entity = OtherTrainingMapper.mapToBudgetEntity(dto, agency);
        budgetRepo.save(entity);
        System.out.println("saved");
        return WorkflowResponse.builder().status(200).message("TrainingBudget saved successfully").data(OtherTrainingMapper.mapToBudgetResponse(entity)).build();

    }

    public WorkflowResponse updateTrainingBudget(Long id, OtherTrainingBudgetDTO dto) throws DataException {
        OtherTrainingBudget existing = budgetRepo.findById(id)
                .orElseThrow(() -> new DataException("TrainingBudget not found with this id : " + id, "TRAINING_BUDGET_NOT_FOUND", 400));

        existing.setPhyTargetAchievement(dto.getPhyTargetAchievement());
        existing.setFinTargetAchievement(dto.getFinTargetAchievement());
        budgetRepo.save(existing);
        return WorkflowResponse.builder().status(200).message("TrainingBudget updated successfully").build();

    }

    public OtherTrainingBudgetDTO getBudgetWithExpenditures(Long id) {
        OtherTrainingBudget budget = budgetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + id));

        return OtherTrainingMapper.mapToBudgetResponse(budget);
    }

    public WorkflowResponse getBudgetsByAgencyId(Long agencyId) {
        List<OtherTrainingBudget> budgets = budgetRepo.findByAgency_AgencyId(agencyId);
        return WorkflowResponse.builder()
                .status(200)
                .message("TrainingBudget updated successfully")
                .data(budgets.stream()
                        .map(OtherTrainingMapper::mapToBudgetResponse)
                        .collect(Collectors.toList()))
                .build();


    }

}
