package com.metaverse.workflow.expenditure.repository;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.BulkExpenditure;
import com.metaverse.workflow.model.HeadOfExpense;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BulkExpenditureRepository extends JpaRepository<BulkExpenditure,Long> {
    BulkExpenditure findByHeadOfExpenseAndItemNameIgnoreCase(HeadOfExpense headOfExpense, String itemName);

    @Query("SELECT DISTINCT b.itemName FROM BulkExpenditure b WHERE b.headOfExpense.expenseId = :expenseId AND b.agency.agencyId = :agencyId")
    List<String> findDistinctItemNamesByHeadOfExpenseAndAgencyId(
            @Param("expenseId") Integer expenseId,
            @Param("agencyId") Long agencyId);

    boolean existsByAgencyAndHeadOfExpenseAndItemName(Agency agency, HeadOfExpense headOfExpense, String itemName);

    boolean existsByAgencyAndHeadOfExpenseAndItemNameAndBulkExpenditureIdNot(Agency agency, HeadOfExpense headOfExpense, String itemName, Long id);

    List<BulkExpenditure> findByAgency_AgencyId(Long agencyId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BulkExpenditure b WHERE b.bulkExpenditureId = :id")
    Optional<BulkExpenditure> findByIdForUpdate(@Param("id") Long id);
}
