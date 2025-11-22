package com.metaverse.workflow.counsellortask.repository;

import com.metaverse.workflow.counsellor.entity.CounsellorTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounsellorTaskRepository extends JpaRepository<CounsellorTask,Long> {
}
