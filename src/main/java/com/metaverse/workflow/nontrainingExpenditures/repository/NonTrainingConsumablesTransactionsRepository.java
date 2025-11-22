package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NonTrainingConsumablesTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NonTrainingConsumablesTransactionsRepository extends JpaRepository<NonTrainingConsumablesTransactions,Long> {
    void deleteByNonTrainingConsumablesBulkId(Long id);
}
