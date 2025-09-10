package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.ListingOnNSE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListingOnNSERepository extends JpaRepository<ListingOnNSE,Long> {
    Optional<List<ListingOnNSE>> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}
