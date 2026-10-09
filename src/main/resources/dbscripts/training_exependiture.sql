select
    ag.agency_id,
    ag.agency_name AS Name_of_the_IA,
    COALESCE(SUM(c.total_bills), 0) AS Total_value_of_Bills_uploaded,
    COALESCE(SUM(c.approved), 0) AS Approved,
    COALESCE(SUM(c.rejected), 0) AS Rejected,
    COALESCE(SUM(c.need_clarification), 0) AS Need_clarification,
    COALESCE(SUM(c.approved + c.rejected + c.need_clarification), 0)
        AS Total_value_of_Bills_verified_under_Training_Activity
FROM agency ag
         LEFT JOIN (
    -- Program Expenditure
    SELECT
        agency_id,
        cost AS total_bills,
        CASE WHEN status = 0 THEN cost ELSE 0 END AS approved,
        CASE WHEN status = 1 THEN cost ELSE 0 END AS rejected,
        CASE WHEN status = 2 THEN cost ELSE 0 END AS need_clarification
    FROM program_expenditure

    UNION ALL

    -- Bulk Expenditure
    SELECT
        agency_id,
        allocated_cost AS total_bills,
        CASE WHEN status = 0 THEN allocated_cost ELSE 0 END AS approved,
        CASE WHEN status = 1 THEN allocated_cost ELSE 0 END AS rejected,
        CASE WHEN status = 2 THEN allocated_cost ELSE 0 END AS need_clarification
    FROM bulk_expenditure_transaction
) c
                   ON ag.agency_id = c.agency_id
GROUP BY ag.agency_id, ag.agency_name
ORDER BY ag.agency_id;