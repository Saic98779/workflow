-- Insert script for Question table
insert into questions(question_id,question_type,question_name,option_value)
values(1,'RadioButton','How is the food Provided','Bad,Avg,good,Not provided');
insert into questions(question_id,question_type,question_name,option_value)
values(2,'RadioButton','How is the trainer','Bad,Avg,Good');
insert into questions(question_id,question_type,question_name,option_value)
values(3,'checkBox','Which facilitys are given their','Watter,Tea/Coffee,Food,snackes');

-- Insert script for call_center_verification_status table
INSERT INTO call_center_verification_status (cc_verification_status_id, cc_verification_details)
VALUES (1, 'Verified'),(2, 'Call not answered'),
       (3, 'Wrong number'),(4, 'Number does not exist'),
       (5, 'Verified data update required from agency'),
       (6, 'Rejected'),(7, 'Not verified');