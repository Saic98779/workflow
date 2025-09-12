package com.metaverse.workflow.program.repository;

import com.metaverse.workflow.model.ProgramSessionFile;
import com.metaverse.workflow.model.TravelAndTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProgramSessionFileRepository extends JpaRepository<ProgramSessionFile, Long> {

    @Query("SELECT p FROM ProgramSessionFile p WHERE p.programSession.programSessionId = :programSessionId")
    List<ProgramSessionFile> findByProgramSessionId(Long programSessionId);


    @Query("SELECT p FROM ProgramSessionFile p WHERE p.programExpenditure.programExpenditureId = :expenditureId")
    List<ProgramSessionFile> findByProgramExpenditureId(Long expenditureId);

    @Query("SELECT p FROM ProgramSessionFile p WHERE p.bulkExpenditure.bulkExpenditureId = :expenditureId")
    List<ProgramSessionFile> findByBulkExpenditureId(Long expenditureId);

    List<ProgramSessionFile> findByProgramExpenditureProgramExpenditureId(Long programExpenditureId);

    List<ProgramSessionFile> findByProgramSession_ProgramSessionIdAndFileType(Long programId, String fileType);

    List<ProgramSessionFile> findByFileType(String string);
    void deleteByProgramExpenditureProgramProgramId(Long programId);

    List<ProgramSessionFile> findByProgramSession_Program_ProgramId(Long programId);

    void deleteByProgramProgramId(Long programId);

    void deleteByNonTrainingResourceExpenditure_NonTrainingResourceExpenditureId(Long id);

    void deleteByNonTrainingExpenditure_Id(Long id);

    void deleteByBenchmarkingStudy_BenchmarkingStudyId(Long id);

    void deleteByNimsmeCentralData_CentralDataId(Long centralDataId);
}
