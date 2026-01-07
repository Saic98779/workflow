package com.metaverse.workflow.participant.repository;

import com.metaverse.workflow.model.InfluencedParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InfluencedParticipantRepository extends JpaRepository<InfluencedParticipant, Long> {
    InfluencedParticipant findByMobileNo(Long mobileNo);

    List<InfluencedParticipant> findByOrganization_OrganizationId(Long organizationId);
}