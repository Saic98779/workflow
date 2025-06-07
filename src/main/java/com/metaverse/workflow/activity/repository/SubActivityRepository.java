package com.metaverse.workflow.activity.repository;

import com.metaverse.workflow.model.SubActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubActivityRepository extends JpaRepository<SubActivity,Long> {
    Optional<SubActivity> findBySubActivityName(String subActivityName);
}
