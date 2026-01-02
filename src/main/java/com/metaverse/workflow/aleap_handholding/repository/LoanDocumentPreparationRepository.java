package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.LoanDocumentPreparation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanDocumentPreparationRepository
        extends JpaRepository<LoanDocumentPreparation, Long> {

    List<LoanDocumentPreparation>
    findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}
