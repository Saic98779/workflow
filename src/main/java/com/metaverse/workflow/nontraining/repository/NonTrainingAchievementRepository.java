package com.metaverse.workflow.nontraining.repository;

import com.metaverse.workflow.model.NonTrainingAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NonTrainingAchievementRepository extends JpaRepository<NonTrainingAchievement,Long> {
//     NonTrainingAchievement findByNonTrainingActivity_activityId(Long activityId);

    List<NonTrainingAchievement> findByNonTrainingActivity_Agency_AgencyId(Long agencyId);
}
