package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ZEDCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ZEDCertificationRepository extends JpaRepository<ZEDCertification, Long> {
    @Query("SELECT DISTINCT zed FROM ZEDCertification zed JOIN zed.participant p WHERE p.participantId = :participantId")
    List<ZEDCertification> findByParticipantId(Long participantId);

    long countByAgencyAgencyIdAndZedCertificationTypeAndCertificationDateBetween(Long agencyId, String bronze, Date dQ1Start, Date dQ1End);

    long countByZedCertificationTypeAndCertificationDateBetween(String type, Date dQ1Start, Date dQ1End);

    long countByZedCertificationType(String bronze);

    default long countZedCertification(Long agencyId, String type, Date dQ1Start, Date dQ1End) {
        if (agencyId == -1) {
            return countByZedCertificationTypeAndCertificationDateBetween(type, dQ1Start, dQ1End);

        } else if (dQ1Start == null || dQ1End == null) {
            return countByZedCertificationType(type);
        } else {
            return countByAgencyAgencyIdAndZedCertificationTypeAndCertificationDateBetween(agencyId, type, dQ1Start, dQ1End);
        }

    }


}
