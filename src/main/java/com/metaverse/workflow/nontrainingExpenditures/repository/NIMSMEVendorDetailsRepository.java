package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NIMSMEVendorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NIMSMEVendorDetailsRepository extends JpaRepository<NIMSMEVendorDetails, Long> {
    List<NIMSMEVendorDetails> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
    void deleteByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}

