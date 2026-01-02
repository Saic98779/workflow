package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.MachineryIdentification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MachineryIdentificationRepository extends JpaRepository<MachineryIdentification, Long> {

    List<MachineryIdentification>
    findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}
