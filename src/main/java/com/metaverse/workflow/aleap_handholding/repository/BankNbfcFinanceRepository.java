package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.BankNbfcFinance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankNbfcFinanceRepository extends JpaRepository<BankNbfcFinance,Long> {
    List<BankNbfcFinance> findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long handholdingSupportId);
}
