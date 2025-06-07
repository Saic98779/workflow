-- Insert Activity SubActivity for TiHCL_05 agency
insert into activity(activity_id,activity_name,agency_id) values(1,'Awareness Program',1),(2,'Training',1);
insert into sub_activity(sub_activity_id,sub_activity_name) values(1,'Revival of Distressed MSMEs'),(2,'Co-Financing'),(3,'Listing under NSE');
insert into activity_sub_activity(activity_id,sub_activity_id)values(1,1),(2,2),(2,3);

-- Insert Activity SubActivity for CoDE_IITH agency
insert into activity(activity_id,activity_name,agency_id) values(3,'Awareness Program',2),(4,'Training',2),(5,'Workshop',2),(6,'Event',2);
insert into sub_activity(sub_activity_id,sub_activity_name) values(4,'To identify the areas of Skill Development');
insert into sub_activity(sub_activity_id,sub_activity_name) values(5,'XR technologies'),(6,'Automotive electronics'),(7,'Game Design'),(8,'3D printing and Design'),(9,'Animation'),
(10,'Graphic Design and Publications'),(11,'Furniture Design'),(12,'Game Design / VFX');
insert into activity_sub_activity(activity_id,sub_activity_id)values(3,4),(4,5),(4,6),(4,7),(4,8),(4,9),(4,10),(4,11),(4,12);
insert into sub_activity(sub_activity_id,sub_activity_name) values(13,'Cheriyal dolls'),(14,'Dokra castings'),(15,'Ikat/ Warangal rugs'),
(16,'Kalamkari/kasuti work of Banjara Embriodary'),(17,'Bamboo crafts'),(18,'Bidri Crafts'),(19,'Ikat textile'),(20,'Kalamkari/kasuti work of Lambada community');
insert into activity_sub_activity(activity_id,sub_activity_id)values(5,13),(5,14),(5,15),(5,16),(5,17),(5,18),(5,19),(5,20);
insert into sub_activity(sub_activity_id,sub_activity_name) values(21,'Research into Design'),(22,'Service Design Conference'),(23,'Research into Design'),(24,'Hackthon');
insert into activity_sub_activity(activity_id,sub_activity_id)values(6,21),(6,22),(6,23),(6,24);

-- Insert Activity SubActivity for RICH_6A agency
insert into activity(activity_id,activity_name,agency_id) values(7,'Workshop',3);
insert into sub_activity(sub_activity_id,sub_activity_name) values(25,'Brain Stoming Workshop'),(26,'Consulation Workshop');
insert into activity_sub_activity(activity_id,sub_activity_id)values(7,25),(7,26);

-- Insert Activity SubActivity for RICH_6B agency
insert into activity(activity_id,activity_name,agency_id) values(8,'Workshop',4),(9,'Awareness Program',4),(10,'Event',4);
INSERT INTO sub_activity (sub_activity_id, sub_activity_name) VALUES(27, 'Roundtable Discussions for Project Strategy and Implementation Planning');
insert into activity_sub_activity(activity_id,sub_activity_id)values(8,27);
INSERT INTO sub_activity (sub_activity_id, sub_activity_name) VALUES(28, 'Awareness on Govt Schemes'),(29, 'Awareness on Skilling'),(30, 'Carbon Footprint Reduction'),
(31, 'Circular Economy Practices'),(32, 'Energy Efficiency'),(33, 'Environmental Regulations and Compliance'),(34, 'Financial Incentives and Grants'),
(35, 'Green Certification and Labels'),(36, 'Green Product Development'),(37, 'Introduction to Green Technologies'),(38, 'Risk Management'),
(39, 'Sustainability Reporting'),(40, 'Sustainable Supply Chain Management'),(41, 'Green Technology Assessment and Selection'),(42, 'Waste Management and Recycling'),
(43, 'Water Conservation and Management'),(44, 'Carbon markets and Carbon compliance'),(45, 'Case Studies Development');
INSERT INTO activity_sub_activity (activity_id, sub_activity_id) VALUE(9, 28),(9, 29),(9, 30),(9, 31),(9, 32),(9, 33),(9, 34),(9, 35),(9, 36),(9, 37),(9, 38),
(9, 39),(9, 40),(9, 41),(9, 42),(9, 43),(9, 44),(9, 45),(9, 46);
INSERT INTO sub_activity (sub_activity_id, sub_activity_name) VALUES(46, 'Hosting Startup Grand Challenge');
insert into activity_sub_activity(activity_id,sub_activity_id)values(10,46);

-- Insert Activity SubActivity for CITD_11 agency
insert into activity(activity_id,activity_name,agency_id) values(11,'Awareness Program',5),(12,'Workshop',5);
insert into sub_activity(sub_activity_id,sub_activity_name) values(47,'Orientation Program on Component')
,(48,'To identify the CFCs & Prototypes'),(105,'Brainstorming session'),(106,'Benchmarking'),(107,'Validation Program for final report');
insert into activity_sub_activity(activity_id,sub_activity_id)values(11,47),(12,48),(12,105),(12,106),(12,107);


-- Insert Activity SubActivity for CIPET_!! agency
insert into activity(activity_id,activity_name,agency_id) values(13,'Awareness Program',6),(14,'Workshop',6);
INSERT INTO sub_activity (sub_activity_id, sub_activity_name) VALUES
(103,'Orientation Program on Component'),
(49, 'To Identify the CFCs & Prototypes for Plastics & Polymers Sector'),
(50, 'To identify the CFCs & Prototypes for Tool, Die & Mould Manufacturers Clusters'),
(51, 'To identify the CFCs & Prototypes for Rubber Products'),
(52, 'To identify the CFCs & Prototypes for Waste Management'),
(53, 'To identify the CFCs & Prototypes for Packaging'),
(54, 'To identify the CFCs & Prototypes for Welding and Fabrication'),
(55, 'To identify the CFCs & Prototypes for Casting & Foundry Cluster'),
(56, 'To identify the CFCs & Prototypes for composite'),
(57, 'To identify the CFCs & Prototypes for Electricals, Electronics & Batteries Sector'),
(58, 'To identify the CFCs & Prototypes for Mechanical & Civil Design Sector');
insert into activity_sub_activity(activity_id,sub_activity_id)values(13,103),(14,49),(14,50),(14,51),(14,52),(14,53),(14,54),(14,55),(14,56),(14,57),(14,58);

-- Insert Activity SubActivity for WeHub agency
insert into activity(activity_id,activity_name,agency_id) values(15,'Awareness Program',7),(16,'Training',7),(17,'Event',7);
INSERT INTO sub_activity (sub_activity_id, sub_activity_name) VALUES
(59, 'Bootcamps to select the Candidates'),(60,'Orientation Program to the Jury Members'),(61,'Orientation Program to the Mentors'),
(62,'Program Grand Launch'),(63,'Graduation Ceremony'),(64,'Orientation between Investors and Enterpreneurs');
insert into activity_sub_activity(activity_id,sub_activity_id)values(15,59),(15,60),(15,61),(15,62),(15,63),(15,64);
INSERT INTO sub_activity (sub_activity_id, sub_activity_name) VALUES
(65,'TOT for SERP and MEPMA APM/DPMs in the districts'),(66,'Capacity Building of Enterpreneurs'),(67,'Demo Day Presentation Trainings'),
(68,'Exposure Visits'),(69,'Women Enterpreneurs Symposium');
insert into activity_sub_activity(activity_id,sub_activity_id)values(16,65),(16,66),(16,67),(16,68),(17,69);

-- Insert Activity SubActivity for NIMSME agency
insert into activity(activity_id,activity_name,agency_id) values(18,'Workshop',8),(19,'Training',8),(20,'Event',8);
INSERT INTO sub_activity (sub_activity_id, sub_activity_name) VALUES(70,'Workshop on Access to Finance'),(71,'Workshop on Market'),(72,'Workshop on Access to Technology and Skilling'),
(73,'Workshop on MSME Promotional Policy, Procedure and Compliance');
insert into activity_sub_activity(activity_id,sub_activity_id)values(18,70),(18,71),(18,72),(18,73);
INSERT INTO sub_activity (sub_activity_id, sub_activity_name) VALUES(74, 'PMEGP'),(75, 'PMMY'),(76, 'TREDS'),(77, 'NSIC'),(78, 'Raw Material'),(79, 'ONDC'),(80, 'GeM'),
(81, 'IC Scheme'),(82, 'GI'),(83, 'Bar Code'),(84, 'LEAN'),(85, 'ZED'),(86, 'Greening MSMEs'),(88, 'Design'),(89, 'Udhyam Registration'),(90, 'Telangana MSME Policy'),
(91, 'PM Viswakarma'),(92, 'National SC/ST Hub'),(93, 'SIDBI ASPIRE'),(94, 'Training on Access to Finance'),(95, 'Traning on Market'),(96, 'Training on Technology and Skilling'),
(97, 'Training on MSME Promotional Policy, Procedure and Compliance'),(98,'Vendor Development Program');
insert into activity_sub_activity(activity_id,sub_activity_id)values(19,74),(19,75),(19,76),(19,77),(19,75),(19,76),(19,77),(19,78),(19,79),(19,80),(19,81),(19,82),
(19,83),(19,84),(19,85),(19,86),(19,88),(19,89),(19,90),(19,91),(19,92),(19,93),(19,94),(19,95),(19,96),(19,97),(20,98);

-- Insert Activity SubActivity for Aleap agency
insert into activity(activity_id,activity_name,agency_id) values(21,'Awareness Program',9),(22,'Training',9);
insert into sub_activity(sub_activity_id,sub_activity_name) values(99,'Awareness to SHGs / Startups'),(100,'MSME Counsellors'),(101,'SHG Members'),(102,'Startups');
insert into activity_sub_activity(activity_id,sub_activity_id)values(21,99),(22,100),(22,101),(22,102);

-- Insert Activity SubActivity for TGTPC-4 agency
insert into activity(activity_id,activity_name,agency_id) values(23,'Awareness Program',10),(24,'Training',10),(25,'Event',10);
insert into sub_activity(sub_activity_id,sub_activity_name) values(103,'Import Substitution');
insert into activity_sub_activity(activity_id,sub_activity_id)values(23,103),(24,103),(25,103);

-- Insert Activity SubActivity for TGTPC-10 agency
insert into activity(activity_id,activity_name,agency_id) values(26,'Awareness Program',11),(27,'Training',11),(28,'Event',11),(29,'RBSM',11);
insert into sub_activity(sub_activity_id,sub_activity_name) values(104,'Export Champions');
insert into activity_sub_activity(activity_id,sub_activity_id)values(26,104),(27,104),(28,104),(29,104);





