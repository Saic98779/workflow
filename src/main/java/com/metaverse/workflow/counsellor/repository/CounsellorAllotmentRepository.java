package com.metaverse.workflow.counsellor.repository;

import com.metaverse.workflow.model.CounsellorAllotment;
import com.metaverse.workflow.model.Mandal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounsellorAllotmentRepository extends JpaRepository<CounsellorAllotment,Long> {

    List<CounsellorAllotment> findByAllotedMandal(Mandal allotedManda);

}
