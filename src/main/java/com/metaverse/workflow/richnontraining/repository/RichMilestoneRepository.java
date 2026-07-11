package com.metaverse.workflow.richnontraining.repository;

import com.metaverse.workflow.model.RichMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RichMilestoneRepository extends JpaRepository<RichMilestone, Long> {

}