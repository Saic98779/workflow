package com.metaverse.workflow.tgtpc_handholding.repository;

import com.metaverse.workflow.model.tgtpc_handholding.IECRegistrationCertification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IECRegistrationCertificationRepository extends JpaRepository<IECRegistrationCertification,Long> {
    List<IECRegistrationCertification> findByTgtpcHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}
