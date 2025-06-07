DELETE FROM program_session_file
WHERE program_expenditure_id IN (
    SELECT program_expenditure_id
    FROM program_expenditure
    WHERE program_id = 1153
);
-- 1. Delete from tables that depend on program_id,1053,1102,1103
DELETE FROM bulk_expenditure_transaction WHERE program_id = 1153;
DELETE FROM media_coverage WHERE program_id = 1153;
DELETE FROM program_expenditure WHERE program_id = 1153;
DELETE FROM program_participant_temp WHERE program_id = 1153;
DELETE FROM program_participant WHERE program_id = 1153;

DELETE FROM program_session_file
WHERE program_session_id IN (
    SELECT program_session_id FROM program_session WHERE program_id = 1153
);

DELETE FROM program_session WHERE program_id = 1153;
DELETE FROM notifications WHERE program = 1153;
DELETE FROM program WHERE program_id = 1153;
