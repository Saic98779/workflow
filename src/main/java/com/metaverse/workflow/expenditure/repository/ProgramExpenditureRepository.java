package com.metaverse.workflow.expenditure.repository;

import com.metaverse.workflow.common.enums.ExpenditureType;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.model.ProgramExpenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ProgramExpenditureRepository extends JpaRepository<ProgramExpenditure,Long> {
    List<ProgramExpenditure> findByExpenditureType(ExpenditureType expenditureType);

    List<ProgramExpenditure> findByExpenditureTypeAndProgram_ProgramId(ExpenditureType expenditureType, Long programId);

    List<ProgramExpenditure> findByExpenditureTypeAndAgency_AgencyIdAndProgram_ProgramId(ExpenditureType expenditureType, Long agencyId, Long programId);

    List<ProgramExpenditure> findByAgency_AgencyIdAndProgram_ProgramId(Long agencyId, Long programId);

    void deleteByProgramProgramId(Long programId);

    Iterable<ProgramExpenditure>
    findBySubActivity_SubActivityIdInAndAgency_AgencyIdAndBillDateBetween(
            Set<Long> subActivityIds,
            Long agencyId,
            Date fromDate,
            Date toDate
    );

    @Query("SELECT SUM(pe.cost) FROM ProgramExpenditure pe " +
            "WHERE pe.agency.agencyId = :agencyId " +
            "AND pe.subActivity.subActivityId = :subActivityId " +
            "AND pe.billDate BETWEEN :startDate AND :endDate")
    Double sumExpenditureByAgencyAndSubActivityAndDateRange(
            @Param("agencyId") Long agencyId,
            @Param("subActivityId") Long subActivityId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );


    Iterable<ProgramExpenditure> findBySubActivity_SubActivityIdInAndAgency_AgencyId(Iterable<Long> subActivityListIDs,Long agencyId);

    Iterable<ProgramExpenditure> findBySubActivity_SubActivityIdIn(Set<Long> subActivityIds);

    List<ProgramExpenditure> findBySubActivity_SubActivityIdAndAgency_AgencyId(Long subActivityListIDs,Long agencyId);

    List<ProgramExpenditure> findByStatusAndProgram_StatusAndAgency_AgencyId(BillRemarksStatus status, String programExpenditureUpdated, long agencyId);
}
