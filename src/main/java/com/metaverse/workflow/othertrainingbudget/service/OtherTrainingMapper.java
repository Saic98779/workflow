package com.metaverse.workflow.othertrainingbudget.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.OtherTrainingBudget;
import com.metaverse.workflow.model.OtherTrainingExpenditure;

import java.util.List;
import java.util.stream.Collectors;

public class OtherTrainingMapper {

    public static OtherTrainingExpenditureDTO toDto(OtherTrainingExpenditure entity) {
        OtherTrainingExpenditureDTO dto = new OtherTrainingExpenditureDTO();
        dto.setId(entity.getId());
        dto.setDateOfExpenditure(DateUtil.dateToString(entity.getDateOfExpenditure(),"dd-MM-yyyy"));
        dto.setDetails(entity.getDetails());
        dto.setAmount(entity.getAmount());
        dto.setBillPath(entity.getBillPath());
        dto.setBillNo(entity.getBillNo());
        dto.setBillDate(DateUtil.dateToString(entity.getBillDate(),"dd-MM-yyyy"));
        dto.setBudgetId(entity.getOtherTrainingBudget().getBudgetId());
        return dto;
    }

    public static OtherTrainingExpenditure toEntity(OtherTrainingExpenditureDTO dto, OtherTrainingBudget trainingBudget) {
        OtherTrainingExpenditure entity = new OtherTrainingExpenditure();
        entity.setDateOfExpenditure(DateUtil.stringToDate(dto.getDateOfExpenditure(),"dd-MM-yyyy"));
        entity.setDetails(dto.getDetails());
        entity.setAmount(dto.getAmount());
        entity.setBillPath(dto.getBillPath());
        entity.setBillNo(dto.getBillNo());
        entity.setBillDate(DateUtil.stringToDate(dto.getBillDate(),"dd-MM-yyyy"));
        entity.setOtherTrainingBudget(trainingBudget);
        return entity;
    }

    public static OtherTrainingBudget mapToBudgetEntity(OtherTrainingBudgetDTO dto, Agency agency) {
        OtherTrainingBudget entity = new OtherTrainingBudget();
        entity.setBudgetId(dto.getBudgetId());
        entity.setBudgetHead(dto.getBudgetHead());
        entity.setPhyTarget(dto.getPhyTarget());
        entity.setPhyTargetAchievement(dto.getPhyTargetAchievement());
        entity.setFinTarget(dto.getFinTarget());
        entity.setFinTargetAchievement(dto.getFinTargetAchievement());
        entity.setAgency(agency);
        return entity;
    }
    public static OtherTrainingBudgetDTO mapToBudgetResponse(OtherTrainingBudget entity) {
        OtherTrainingBudgetDTO dto = new OtherTrainingBudgetDTO();
        dto.setBudgetId(entity.getBudgetId());
        dto.setBudgetHead(entity.getBudgetHead());
        dto.setPhyTarget(entity.getPhyTarget());
        dto.setPhyTargetAchievement(entity.getPhyTargetAchievement());
        dto.setFinTarget(entity.getFinTarget());
        dto.setFinTargetAchievement(entity.getFinTargetAchievement());

        List<OtherTrainingExpenditureDTO> expenditureDTOs = entity.getOtherTrainingExpenditures()
                .stream()
                .map(OtherTrainingMapper::toDto)
                .collect(Collectors.toList());
        try {
            double phyTarget = entity.getPhyTarget() != null ? entity.getPhyTarget() : 0.0;
            double phyAch = entity.getPhyTargetAchievement() != null
                    ? Double.parseDouble(entity.getPhyTargetAchievement())
                    : 0.0;

            if (phyTarget > 0) {
                double phyPerc = (phyAch / phyTarget) * 100;
                dto.setPhyPercentage(String.format("%.2f", phyPerc) + "%");
            } else {
                dto.setPhyPercentage("NA");
            }
        } catch (NumberFormatException e) {
            dto.setPhyPercentage("NA");
        }

        // Calculate Financial Percentage
        try {
            double finTarget = entity.getFinTarget() != null ? entity.getFinTarget() : 0.0;
            double finAch = entity.getFinTargetAchievement() != null
                    ?entity.getFinTargetAchievement()
                    : 0.0;

            if (finTarget > 0) {
                double finPerc = (finAch / finTarget) * 100;
                dto.setFinPercentage(String.format("%.2f", finPerc) + "%");
            } else {
                dto.setFinPercentage("NA");
            }
        } catch (NumberFormatException e) {
            dto.setFinPercentage("NA");
        }
        dto.setOtherTrainingExpenditures(expenditureDTOs);
        return dto;
    }
}
