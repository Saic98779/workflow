package com.metaverse.workflow.program.repository;

import com.metaverse.workflow.model.ProgramSessionFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Query("SELECT p FROM ProgramSessionFile p WHERE p.programSession.program.programId = :programId AND LOWER(TRIM(p.fileType)) = LOWER(TRIM(:fileType))")
    List<ProgramSessionFile> findByProgramSession_Program_ProgramIdAndFileTypeIgnoreCase(@Param("programId") Long programId, @Param("fileType") String fileType);

    @Query("SELECT p FROM ProgramSessionFile p WHERE p.program.programId = :programId AND LOWER(TRIM(p.fileType)) = LOWER(TRIM(:fileType))")
    List<ProgramSessionFile> findByProgramProgramIdAndFileTypeIgnoreCase(@Param("programId") Long programId, @Param("fileType") String fileType);

    // Relaxed matching: LIKE (case-insensitive) on fileType
    @Query("SELECT p FROM ProgramSessionFile p WHERE p.programSession.program.programId = :programId AND LOWER(TRIM(p.fileType)) LIKE CONCAT('%', LOWER(TRIM(:fileType)), '%')")
    List<ProgramSessionFile> findByProgramSession_Program_ProgramIdAndFileTypeLikeIgnoreCase(@Param("programId") Long programId, @Param("fileType") String fileType);

    @Query("SELECT p FROM ProgramSessionFile p WHERE p.program.programId = :programId AND LOWER(TRIM(p.fileType)) LIKE CONCAT('%', LOWER(TRIM(:fileType)), '%')")
    List<ProgramSessionFile> findByProgramProgramIdAndFileTypeLikeIgnoreCase(@Param("programId") Long programId, @Param("fileType") String fileType);

    // case-sensitive variant if needed
    List<ProgramSessionFile> findByProgramSession_Program_ProgramIdAndFileType(Long programId, String fileType);

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

    Optional<ProgramSessionFile> findByProgramProgramIdAndProgramSessionFileIdAndFileType(Long programId, Long fileId, String collage);

    void deleteByFormalisationCompliance_FormalisationComplianceId(Long id);

    void deleteByNimsmeVDP_NimsmeVdpId(Long nimsmeVdpId);

}
