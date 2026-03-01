package com.metaverse.workflow.visitorcount.repository;

import com.metaverse.workflow.model.VisitorCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorCountRepository extends JpaRepository<VisitorCount, Long> {

    /**
     * Find the primary visitor count record
     * @return the visitor count entity
     */
    Optional<VisitorCount> findById(Long id);
}

