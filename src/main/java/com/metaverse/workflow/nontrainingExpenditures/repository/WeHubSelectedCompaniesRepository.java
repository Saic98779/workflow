package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.WeHubSelectedCompanies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeHubSelectedCompaniesRepository extends JpaRepository<WeHubSelectedCompanies,Long> {

    Optional<List<WeHubSelectedCompanies>> findByNonTrainingSubActivity_SubActivityId(Long subActivityId);
}
