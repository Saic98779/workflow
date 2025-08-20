package com.metaverse.workflow.nontraining.repository;

import com.metaverse.workflow.model.NonTrainingAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonTrainingAchievementRepository extends JpaRepository<NonTrainingAchievement,Long> {
     NonTrainingAchievement findByNonTrainingActivity_activityId(Long activityId);
}
