package com.metaverse.workflow.audit.bulk.counter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface BulkTableCounterRepository extends JpaRepository<BulkTableCounter, Long> {

    Optional<BulkTableCounter> findByTableName(String tableName);

    List<BulkTableCounter> findAllByOrderByTableNameAsc();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update BulkTableCounter c "
            + "set c.insertCount = c.insertCount + :inserts, "
            + "c.updateCount = c.updateCount + :updates, "
            + "c.deleteCount = c.deleteCount + :deletes, "
            + "c.rowCount = c.rowCount + :inserts - :deletes, "
            + "c.lastAction = :action, c.lastActionAt = :at, c.updatedAt = :at "
            + "where c.tableName = :tableName")
    int bumpCounters(@Param("tableName") String tableName,
                     @Param("inserts") long inserts,
                     @Param("updates") long updates,
                     @Param("deletes") long deletes,
                     @Param("action") String action,
                     @Param("at") Instant at);
}
