The schema for the database is in the resources directory.
The credentials to use the database are found in the App file, under the ats directory.
For the database and application to work as intended url, user and password must be supplied.

The schema has been exported with no data, following advice from our consultant,
but please note that to run properly the program expects certain fields to be in the database already.
These are as follows:
In the blank_status table, the field blank_status should have the following queries applied:

INSERT INTO `ats`.`blank_status` (`blank_status`) VALUES ('ASGN');
INSERT INTO `ats`.`blank_status` (`blank_status`) VALUES ('AVBL');
INSERT INTO `ats`.`blank_status` (`blank_status`) VALUES ('RFND');
INSERT INTO `ats`.`blank_status` (`blank_status`) VALUES ('RMVD');
INSERT INTO `ats`.`blank_status` (`blank_status`) VALUES ('SOLD');
INSERT INTO `ats`.`blank_status` (`blank_status`) VALUES ('VOID');

In the role table, the fields role_code and role_name should have the following queries applied:

INSERT INTO `ats`.`role` (`role_code`, `role_name`) VALUES ('OM', 'Office Manager');
INSERT INTO `ats`.`role` (`role_code`, `role_name`) VALUES ('SA', 'System Administrator');
INSERT INTO `ats`.`role` (`role_code`, `role_name`) VALUES ('TA', 'Travel Agent');

In the customer_status table, the status_code and status_name fields should have the following queries applied:

INSERT INTO `ats`.`customer_status` (`status_code`, `status_name`) VALUES ('RG', 'Regular Customer');
INSERT INTO `ats`.`customer_status` (`status_code`, `status_name`) VALUES ('VD', 'Valued Customer');

In the blank_type table, the blank_type field should have the following queries applied:

INSERT INTO `ats`.`blank_type` (`blank_type`) VALUES ('101');
INSERT INTO `ats`.`blank_type` (`blank_type`) VALUES ('201');
INSERT INTO `ats`.`blank_type` (`blank_type`) VALUES ('420');
INSERT INTO `ats`.`blank_type` (`blank_type`) VALUES ('440');
INSERT INTO `ats`.`blank_type` (`blank_type`) VALUES ('444');

All of the rest should be auto generating on it's own, though it may be necessary to manually insert a System Administrator
if all actions are to be taken externally, as they are the only person who can create a new user, and without a user account
access cannot be granted.
