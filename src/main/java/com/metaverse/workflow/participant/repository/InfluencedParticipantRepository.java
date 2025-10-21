package com.metaverse.workflow.participant.repository;

import com.metaverse.workflow.model.InfluencedParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfluencedParticipantRepository extends JpaRepository<InfluencedParticipant, Long> {
}