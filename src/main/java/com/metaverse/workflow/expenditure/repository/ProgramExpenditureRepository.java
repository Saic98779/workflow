package com.metaverse.workflow.expenditure.repository;

import com.metaverse.workflow.common.enums.ExpenditureType;
import com.metaverse.workflow.model.ProgramExpenditure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramExpenditureRepository extends JpaRepository<ProgramExpenditure,Long> {
    List<ProgramExpenditure> findByExpenditureType(ExpenditureType expenditureType);

    List<ProgramExpenditure> findByExpenditureTypeAndProgram_ProgramId(ExpenditureType expenditureType, Long programId);

    List<ProgramExpenditure> findByExpenditureTypeAndAgency_AgencyIdAndProgram_ProgramId(ExpenditureType expenditureType, Long agencyId,Long programId);
    List<ProgramExpenditure> findByAgency_AgencyIdAndProgram_ProgramId(Long agencyId,Long programId);
    void deleteByProgramProgramId(Long programId);
}
