package com.metaverse.workflow.nontraining.repository;

import com.metaverse.workflow.model.NonTrainingAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NonTrainingAchievementRepository extends JpaRepository<NonTrainingAchievement,Long> {
    NonTrainingAchievement findByNonTrainingActivity_activityId(Long activityId);

    Optional<NonTrainingAchievement> findByNonTrainingSubActivity_SubActivityId(Long activityId);

    List<NonTrainingAchievement> findByNonTrainingActivity_Agency_AgencyId(Long agencyId);

    NonTrainingAchievement findByNonTrainingSubActivity_subActivityId(Long subActivityId);
}
