package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.BenchmarkingStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BenchmarkingStudyRepository extends JpaRepository<BenchmarkingStudy,Long> {
    Optional<List<BenchmarkingStudy>> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}
