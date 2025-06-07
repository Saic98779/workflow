-- Insert agencies script
-- insert into agency (agency_id, agency_name) values (1,'ALEAP'),(2,'WeHub'),(3,'TIHCL'),(4,'Commissionarate of Industries'),(5,'GT');
insert into agency (agency_id, agency_name) values (1,'ALEAP'),(2,'WeHub'),(3,'TIHCL');

-- Insert users
INSERT INTO user (user_id,address,attempts,created_on,email,first_name,gender,last_name,mobile_no,password,status,updated_on,user_role,agency_id) VALUES ('admin@gmail.com',NULL,NULL,'2025-04-01 21:39:32.260000','admin@gmail.com','Murthy','Male','NV',9948022300,'Password@123',NULL,NULL,'ADMIN',NULL);
INSERT INTO user (user_id,address,attempts,created_on,email,first_name,gender,last_name,mobile_no,password,status,updated_on,user_role,agency_id) VALUES ('aleap@gmail.com',NULL,NULL,'2025-04-01 21:39:43.193000','aleap@gmail.com','Aleap','Male','E',9948022301,'Password@123',NULL,NULL,'AGENCY_MANAGER',1);
INSERT INTO user (user_id,address,attempts,created_on,email,first_name,gender,last_name,mobile_no,password,status,updated_on,user_role,agency_id) VALUES ('tihcl@gmail.com',NULL,NULL,'2025-04-01 21:39:15.296000','tihcl@gmail.com','TIHCL','Male','E',9948022303,'Password@123',NULL,NULL,'AGENCY_MANAGER',3);
INSERT INTO user (user_id,address,attempts,created_on,email,first_name,gender,last_name,mobile_no,password,status,updated_on,user_role,agency_id) VALUES ('WeHub@gmail.com',NULL,NULL,'2025-04-01 21:39:56.053000','WeHub@gmail.com','WeHub','Male','W',9948022302,'Password@123',NULL,NULL,'AGENCY_MANAGER',2);

-- Insert program outcome tables
insert into program_outcome_tables (outcome_table_id, outcome_table_display_name, outcome_table_name) values
(1, 'ONDC Registration', 'ONDCRegistration'),
(2, 'ONDC Transaction', 'ONDCTransaction'),
(3, 'Udyam Registration', 'UdyamRegistration'),
(4, 'CGTMSE Transaction', 'CGTMSETransaction');

-- Insert district and mandal script
insert into district(district_id,district_name) values (1,'Adilabad'),(2,'Hyderabad');
insert into mandal(mandal_id,mandal_name,district) values(1,'Adilabad(Rural)',1),(2,'Adilabad(urbarn)',1);

-- Insert Activity & Sub Activity table script
insert into activity(activity_id,activity_name,agency_id) values(1,'Sample Activity1',1);
insert into sub_activity(sub_activity_id,sub_activity_name) values(1,'Sample Sub Activity1');
insert into activity(activity_id,activity_name,agency_id) values(2,'Sample Activity2',1);
insert into sub_activity(sub_activity_id,sub_activity_name) values(2,'Sample Sub Activity2');
insert into activity_sub_activity(activity_id,sub_activity_id) values(1,1);
insert into activity_sub_activity(activity_id,sub_activity_id) values(2,2);

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
