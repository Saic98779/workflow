package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ECommerceRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ECommerceRegistrationRepository extends JpaRepository<ECommerceRegistration,Long> {
    ECommerceRegistration findByInfluencedParticipant_InfluencedId(Long participantId);

    boolean existsByInfluencedParticipant_InfluencedId(Long influencedId);

    boolean existsByParticipant_ParticipantId(Long participantId);

    ECommerceRegistration findByParticipant_ParticipantId(Long participantId);

    List<ECommerceRegistration> findByAgencyAgencyId(Long agencyId);

    long countByAgencyAgencyIdAndDateOfOnboardingBetween(Long agencyId, Date start, Date end);
    long countByDateOfOnboardingBetween(Date start, Date end);

    default long countECommerceRegistration(Long agencyId, Date start, Date end) {
        if (agencyId == -1) {
            return countByDateOfOnboardingBetween(start, end);
        } else if (start == null || end == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndDateOfOnboardingBetween(agencyId, start, end);
        }
    }
}
