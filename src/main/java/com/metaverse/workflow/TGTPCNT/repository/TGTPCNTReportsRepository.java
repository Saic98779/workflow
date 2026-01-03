package com.metaverse.workflow.TGTPCNT.repository;

import com.metaverse.workflow.model.TGTPCNTReports;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TGTPCNTReportsRepository extends JpaRepository<TGTPCNTReports, Long> {
    List<TGTPCNTReports> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}
