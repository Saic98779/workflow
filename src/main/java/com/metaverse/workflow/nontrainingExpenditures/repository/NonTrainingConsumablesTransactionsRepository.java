package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NonTrainingConsumablesTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NonTrainingConsumablesTransactionsRepository extends JpaRepository<NonTrainingConsumablesTransactions,Long> {
    void deleteByNonTrainingConsumablesBulkId(Long id);
    List<NonTrainingConsumablesTransactions> findByNonTrainingConsumablesBulk_NonTrainingSubActivity_SubActivityId(Long subActivityId);

}
