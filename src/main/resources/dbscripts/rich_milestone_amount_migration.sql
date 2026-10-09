-- ============================================================================
-- Migration: RichMilestone amount tracking
--            + NonTrainingExpenditure <-> RichMilestone relation change
-- NOTE: spring.jpa.hibernate.ddl-auto=none, so run this script manually.
-- ============================================================================

-- 1. New amount columns on rich_milestone -----------------------------------
ALTER TABLE rich_milestone ADD COLUMN consumed_amount DOUBLE PRECISION NULL DEFAULT 0;
ALTER TABLE rich_milestone ADD COLUMN available_amount DOUBLE PRECISION NULL;

-- Backfill: every existing milestone starts unconsumed, fully available
UPDATE rich_milestone SET consumed_amount = 0 WHERE consumed_amount IS NULL;
UPDATE rich_milestone SET available_amount = amount WHERE available_amount IS NULL;

-- 2. New single-milestone FK column on non_training_expenditure ------------
ALTER TABLE non_training_expenditure ADD COLUMN rich_milestone_id BIGINT NULL;

-- Backfill the link from the old rich_milestone.nonTrainingExpenditure column
-- (a milestone could previously belong to an expenditure; keep the first one)
UPDATE non_training_expenditure e
   SET e.rich_milestone_id = (
         SELECT MIN(m.rich_milestone_id)
           FROM rich_milestone m
          WHERE m.nonTrainingExpenditure = e.id
       )
 WHERE e.rich_milestone_id IS NULL;

-- Optional: recompute milestone amounts from the linked expenditures
-- UPDATE rich_milestone m
--    SET consumed_amount = COALESCE((SELECT SUM(e.expenditure_amount)
--                                      FROM non_training_expenditure e
--                                     WHERE e.rich_milestone_id = m.rich_milestone_id), 0),
--        available_amount = m.amount - COALESCE((SELECT SUM(e.expenditure_amount)
--                                                  FROM non_training_expenditure e
--                                                 WHERE e.rich_milestone_id = m.rich_milestone_id), 0);

-- 3. Drop the old relationship column from rich_milestone ------------------
ALTER TABLE rich_milestone DROP COLUMN nonTrainingExpenditure;

-- 4. Optional FK constraints (uncomment if your DB doesn't use ddl-auto/keys)
-- ALTER TABLE non_training_expenditure
--   ADD CONSTRAINT fk_nte_rich_milestone
--   FOREIGN KEY (rich_milestone_id) REFERENCES rich_milestone (rich_milestone_id);
