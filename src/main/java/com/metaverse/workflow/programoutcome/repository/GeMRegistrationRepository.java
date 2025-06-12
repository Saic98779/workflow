package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.GeMRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeMRegistrationRepository extends JpaRepository<GeMRegistration,Long> {
    boolean existsByParticipant_ParticipantId(Long participantId);

    GeMRegistration findByParticipantParticipantId(Long participantId);
}
