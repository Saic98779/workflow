package com.metaverse.workflow.programoutcome.repository;

import com.metaverse.workflow.model.outcomes.GeMTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeMTransactionRepository extends JpaRepository<GeMTransaction,Long> {
}
