package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.eCommerceRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface eCommerceRegistrationRepository extends JpaRepository<eCommerceRegistration,Long> {
    eCommerceRegistration findByInfluencedParticipant_InfluencedId(Long participantId);

    boolean existsByInfluencedParticipant_InfluencedId(Long influencedId);

    boolean existsByParticipant_ParticipantId(Long participantId);

    eCommerceRegistration findByParticipant_ParticipantId(Long participantId);
}
