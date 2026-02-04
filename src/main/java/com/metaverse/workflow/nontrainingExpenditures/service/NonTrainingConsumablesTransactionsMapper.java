package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.NonTrainingConsumablesTransactions;

public class NonTrainingConsumablesTransactionsMapper {

    public static NonTrainingConsumablesTransactions mapToTransaction(NonTrainingConsumablesTransactionsDTO dto) {
        return NonTrainingConsumablesTransactions.builder()
                .dateOfUtilisation(DateUtil.covertStringToDate(dto.getDateOfUtilisation()))
                .quantityOfUtilisation(dto.getQuantityOfUtilisation())
                .noOfTraineesUtilised(dto.getNoOfTraineesUtilised())
                .build();
    }

    public static NonTrainingConsumablesTransactions updateTransaction(
            NonTrainingConsumablesTransactions existing,
            NonTrainingConsumablesTransactionsDTO dto) {

        existing.setDateOfUtilisation(DateUtil.covertStringToDate(dto.getDateOfUtilisation()));
        existing.setQuantityOfUtilisation(dto.getQuantityOfUtilisation());
        existing.setNoOfTraineesUtilised(dto.getNoOfTraineesUtilised());
        return existing;
    }

    public static NonTrainingConsumablesTransactionsDTO mapToTransactionDto(
            NonTrainingConsumablesTransactions entity) {

        return NonTrainingConsumablesTransactionsDTO.builder()
                .id(entity.getId())
                .programId(entity.getProgram() != null ?entity.getProgram().getProgramId():null)
                .programName(entity.getProgram() != null ?entity.getProgram().getProgramTitle():null)
                .dateOfUtilisation(entity.getDateOfUtilisation().toString())
                .quantityOfUtilisation(entity.getQuantityOfUtilisation())
                .noOfTraineesUtilised(entity.getNoOfTraineesUtilised())
                .bulkId(entity.getNonTrainingConsumablesBulk().getId())
                .build();
    }
}
