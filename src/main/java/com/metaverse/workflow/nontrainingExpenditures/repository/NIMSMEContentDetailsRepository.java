package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NIMSMEContentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NIMSMEContentDetailsRepository extends JpaRepository<NIMSMEContentDetails, Long> {
    List<NIMSMEContentDetails> findByContentType(String contentType);
    List<NIMSMEContentDetails> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
    void deleteByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}
