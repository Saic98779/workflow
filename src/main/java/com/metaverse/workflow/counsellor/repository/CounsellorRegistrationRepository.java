package com.metaverse.workflow.counsellor.repository;

import com.metaverse.workflow.model.CounsellorRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface CounsellorRegistrationRepository extends JpaRepository<CounsellorRegistration,Long> {


}
