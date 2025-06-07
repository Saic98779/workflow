package com.metaverse.workflow.callcenter.repository;

import com.metaverse.workflow.model.SubActivityQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubActivityQuestionsRepository extends JpaRepository<SubActivityQuestions,Long> {


    SubActivityQuestions findBySubActivityId(Long subActivityId);
}
