package com.metaverse.workflow.callcenter.repository;

import com.metaverse.workflow.model.CallCenterVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;


public interface CallCenterVerificationRepository extends JpaRepository<CallCenterVerification, Long> {

    @Query("FROM CallCenterVerification  WHERE programId = :programId")
    List<CallCenterVerification> findByProgramId(Long programId);

    Optional<CallCenterVerification> findByProgramIdAndParticipantId(Long programId, Long participantId);

}
