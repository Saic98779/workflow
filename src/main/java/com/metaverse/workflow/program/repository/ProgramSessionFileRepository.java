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

    void deleteByNimsmeVendorDetails_Id(Long vendorId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProgramSessionFile p SET p.filePath = :filePath WHERE p.travelAndTransport.travelTransportId = :travelTransportId")
    int updateFilePathByTravelTransportId(@Param("filePath") String filePath, @Param("travelTransportId") Long travelTransportId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProgramSessionFile p SET p.filePath = :filePath WHERE p.nimsmeVendorDetails.id = :vendorDetailsId")
    int updateFilePathByVendorDetailsId(@Param("filePath") String filePath, @Param("vendorDetailsId") Long vendorDetailsId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProgramSessionFile p SET p.filePath = :filePath WHERE p.nonTrainingExpenditure.id = :nonTrainingExpenditureId")
    int updateFilePathByNonTrainingExpenditureId(@Param("filePath") String filePath, @Param("nonTrainingExpenditureId") Long nonTrainingExpenditureId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProgramSessionFile p SET p.filePath = :filePath WHERE p.nonTrainingResourceExpenditure.nonTrainingResourceExpenditureId = :nonTrainingResourceExpenditureId")
    int updateFilePathByNonTrainingResourceExpenditureId(@Param("filePath") String filePath, @Param("nonTrainingResourceExpenditureId") Long nonTrainingResourceExpenditureId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ProgramSessionFile p SET p.filePath = :filePath WHERE p.nimsmeCentralData.centralDataId = :centralDataId")
    int updateFilePathByCentralDataId(@Param("filePath") String filePath, @Param("centralDataId") Long centralDataId);

    @Modifying
    @Query("update ProgramSessionFile p set p.filePath = :filePath where p.nonTrainingConsumablesBulk.id = :bulkId")
    int updateFilePathByNonTrainingConsumablesBulkId(@Param("filePath") String filePath,
                                                      @Param("bulkId") Long bulkId);


    void deleteByNonTrainingConsumablesBulkId(Long id);
}
