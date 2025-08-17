package com.metaverse.workflow.nontraining.repository;

import com.metaverse.workflow.model.NonTrainingAchievement;
import com.metaverse.workflow.model.NonTrainingActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NonTrainingActivityRepository extends JpaRepository<NonTrainingActivity,Long> {
    List<NonTrainingActivity> findByAgency_AgencyId(Long agencyId);
}