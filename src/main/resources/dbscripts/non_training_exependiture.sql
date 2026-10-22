SELECT
    IFNULL((
               SELECT SUM(expenditure_amount)
               FROM non_training_expenditure
               WHERE agency_id = 2
           ), 0)
        +
    IFNULL((
               SELECT SUM(amount)
               FROM non_training_resource_expenditure
               WHERE resource_id IN (
                   SELECT resource_id
                   FROM non_training_resources
                   WHERE activity_id IN (
                       SELECT activity_id
                       FROM non_training_activity
                       WHERE agency_id = 2
                   )
               )
           ), 0)
        +
    IFNULL((
               SELECT SUM(amount)
               FROM travel_and_transport
               WHERE activity_id IN (
                   SELECT activity_id
                   FROM non_training_activity
                   WHERE agency_id = 2
               )
           ), 0)
        AS total_non_training_cost;
SELECT
    a.agency_id,

    IFNULL((
               SELECT SUM(nte.expenditure_amount)
               FROM non_training_expenditure nte
               WHERE nte.agency_id = a.agency_id
           ), 0)
        +
    IFNULL((
               SELECT SUM(nre.amount)
               FROM non_training_resource_expenditure nre
               WHERE nre.resource_id IN (
                   SELECT nr.resource_id
                   FROM non_training_resources nr
                   WHERE nr.activity_id IN (
                       SELECT nta.activity_id
                       FROM non_training_activity nta
                       WHERE nta.agency_id = a.agency_id
                   )
               )
           ), 0)
        +
    IFNULL((
               SELECT SUM(tt.amount)
               FROM travel_and_transport tt
               WHERE tt.activity_id IN (
                   SELECT nta.activity_id
                   FROM non_training_activity nta
                   WHERE nta.agency_id = a.agency_id
               )
           ), 0) AS total_non_training_cost

FROM (
         SELECT agency_id FROM non_training_activity
         UNION
         SELECT agency_id FROM non_training_expenditure
     ) a
ORDER BY a.agency_id;




