package com.metaverse.workflow.expenditure.repository;

import com.metaverse.workflow.model.BulkExpenditureTransaction;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface BulkExpenditureTransactionRepository extends JpaRepository<BulkExpenditureTransaction,Long> {

    List<BulkExpenditureTransaction> findByProgram_ProgramId(Long programId);
    void deleteByExpenditureBulkExpenditureId(Long expenditureId);
    void deleteByProgramProgramId(Long programId);

    List<BulkExpenditureTransaction> findByProgram_ProgramIdIn(List<Long> programId);
    List<BulkExpenditureTransaction> findByExpenditure_BulkExpenditureId(Long bulkExpenditureId);

    @Query("select t from BulkExpenditureTransaction t where t.id = :transactionId")
    Optional<BulkExpenditureTransaction> findByIdForUpdate(@Param("transactionId") Long transactionId);
}
