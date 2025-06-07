package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ONDCRegistration;
import com.metaverse.workflow.model.outcomes.TReDSRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TReDSRegistrationRepository extends JpaRepository<TReDSRegistration,Integer> {
    @Query("SELECT DISTINCT treds FROM TReDSRegistration treds JOIN treds.participant p WHERE p.participantId = :participantId")
    List<TReDSRegistration> findByParticipantId(Long participantId);

    @Query("SELECT COUNT(r) FROM TReDSRegistration r WHERE r.agency.agencyId = :agencyId AND r.tredsRegistrationDate BETWEEN :startDate AND :endDate")
    long countByAgencyIdAndTredsRegistrationDateBetween(@Param("agencyId") Long agencyId,
                                                        @Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate);
    @Query("SELECT COUNT(r) FROM TReDSRegistration r WHERE r.tredsRegistrationDate BETWEEN :startDate AND :endDate")
    long countByTredsRegistrationDateBetween(@Param("startDate") Date startDate,
                                                @Param("endDate") Date endDate);


    boolean existsByParticipant_ParticipantId(Long participantId);

    default long countTReDSRegistration(Long agencyId, Date dQ1Start, Date dQ1End) {
        if (agencyId == -1) {
            return countByTredsRegistrationDateBetween(dQ1Start, dQ1End);
        } else if (dQ1Start == null || dQ1End == null) {
            return count();
        } else {
            return countByAgencyIdAndTredsRegistrationDateBetween(agencyId, dQ1Start, dQ1End);
        }

    }
}