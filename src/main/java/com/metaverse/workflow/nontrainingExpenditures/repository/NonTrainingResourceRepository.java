package com.metaverse.workflow.nontrainingExpenditures.repository;

import com.metaverse.workflow.model.NonTrainingResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface NonTrainingResourceRepository extends JpaRepository<NonTrainingResource, Long> {


    @Query("SELECT COALESCE(SUM(ntrEx.amount), 0) " +
            "FROM NonTrainingResource ntr " +
            "JOIN ntr.nonTrainingResourceExpenditures ntrEx " +
            "JOIN ntr.nonTrainingSubActivity ntsa " +
            "JOIN ntsa.nonTrainingActivity nta " +
            "WHERE nta.activityName = :activityName")
    Double sumExpenditureByActivityName(@Param("activityName") String activityName);
}

