package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.GeMRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface GeMRegistrationRepository extends JpaRepository<GeMRegistration, Long> {
    boolean existsByParticipant_ParticipantId(Long participantId);

    GeMRegistration findByParticipantParticipantId(Long participantId);

    long countByAgencyAgencyIdAndGemRegistrationDateBetween(Long agencyId, Date dQ1Start, Date dQ1End);

    long countByGemRegistrationDateBetween(Date dQ1Start, Date dQ1End);

    default long countGeMRegistration(Long agencyId, Date dQ1Start, Date dQ1End) {
        if (agencyId == -1) {
            return countByGemRegistrationDateBetween(dQ1Start, dQ1End);
        } else if (dQ1Start == null || dQ1End == null) {
            return count();
        } else {
            return countByAgencyAgencyIdAndGemRegistrationDateBetween(agencyId, dQ1Start, dQ1End);
        }
    }


    List<GeMRegistration> findByAgencyAgencyId(Long agencyId);
}
