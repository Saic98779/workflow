package com.metaverse.workflow.programoutcometargets.repository;

import com.metaverse.workflow.model.FinancialTarget;
import com.metaverse.workflow.model.PhysicalTarget;
import com.metaverse.workflow.model.outcomes.ProgramOutcomeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhysicalRepository extends JpaRepository<PhysicalTarget,Long> {
    List<PhysicalTarget> findByAgencyAgencyId(Long agencyId);

    List<PhysicalTarget> findByProgramOutcomeTableAndFinancialYear(ProgramOutcomeTable outcomeTable, String financialYear);

    List<PhysicalTarget> findByProgramOutcomeTableAndFinancialYearAndAgencyAgencyId(ProgramOutcomeTable outcomeTable, String financialYear, Long agencyId);

    List<PhysicalTarget> findByFinancialYear(String financialYear);

    @Query("SELECT pt FROM PhysicalTarget pt " +
            "WHERE pt.programOutcomeTable.outcomeTableName = :outcomeName " +
            "AND pt.financialYear = :financialYear " +
            "AND pt.agency.agencyId = :agencyId")
    PhysicalTarget findTarget(@Param("outcomeName") String outcomeName,
                              @Param("financialYear") String financialYear,
                              @Param("agencyId") Long agencyId);

    @Query("SELECT pt FROM PhysicalTarget pt " +
            "WHERE pt.programOutcomeTable.outcomeTableName = :outcomeName " +
            "AND pt.programOutcomeTable.outcomeTableId = :outcomeId " +
            "AND pt.financialYear = :financialYear " +
            "AND pt.agency.agencyId = :agencyId")
    PhysicalTarget findTarget(@Param("outcomeName") String outcomeName,
                              @Param("outcomeId") Integer outcomeId,
                              @Param("financialYear") String financialYear,
                              @Param("agencyId") Long agencyId);


}
