package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.ONDCRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ONDCRegistrationRepository extends JpaRepository<ONDCRegistration, Long> {
    @Query("SELECT DISTINCT ondc FROM ONDCRegistration ondc JOIN ondc.participant p WHERE p.participantId = :participantId")
    List<ONDCRegistration> findByParticipantId(Long participantId);

    @Query("SELECT COUNT(r) FROM ONDCRegistration r " +
            "WHERE r.agency.agencyId = :agencyId " +
            "AND r.ondcRegistrationDate BETWEEN :startDate AND :endDate")
    long countByAgencyAndDateBetween(@Param("agencyId") Long agencyId,
                                     @Param("startDate") Date startDate,
                                     @Param("endDate") Date endDate);
    @Query("SELECT COUNT(r) FROM ONDCRegistration r " +
            "WHERE r.ondcRegistrationDate BETWEEN :startDate AND :endDate")
    long countByDateBetween(@Param("startDate") Date startDate,
                            @Param("endDate") Date endDate);
    boolean existsByParticipant_ParticipantId(Long participantId);
    default long countONDCRegistration(Long agencyId, Date dQ1Start, Date dQ1End) {
        if (agencyId == -1) {
            return countByDateBetween(dQ1Start, dQ1End);
        } else if (dQ1Start == null || dQ1End == null) {
            return count();
        } else {
            return countByAgencyAndDateBetween(agencyId, dQ1Start, dQ1End);
        }

    }

}
