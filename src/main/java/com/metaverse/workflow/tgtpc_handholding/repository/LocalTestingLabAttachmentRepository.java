package com.metaverse.workflow.tgtpc_handholding.repository;

import com.metaverse.workflow.model.tgtpc_handholding.LocalTestingLabAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocalTestingLabAttachmentRepository extends JpaRepository<LocalTestingLabAttachment,Long> {
    List<LocalTestingLabAttachment> findByTgtpcHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}
