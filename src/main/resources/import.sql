--H2 Database native query
--insert into additional_user_details (is_active, resident_status, user_role,no_of_ppl) values ('TRUE', 'OWNER', 'SUPER_ADMIN', 0);
----insert into owner_details (owner_firstname, owner_lastname, owner_email_addr,owner_phone_nbr,owner_addr) values ("","","","","");
--insert into resident_users (additional_user_details_system_addl_user_id, email_addr, firstname, flat_number, lastname, password, pending_amount, phone_nbr, residing_since, creation_date, updation_date, gender, date_of_birth) values (1, 'super.admin@svlakeview.com', 'Super', 0, 'Admin', 'e6e061838856bf47e1de73719fb269', 0.0, '000', '2016-12-12', sysdate, sysdate, 'MALE', '2016-12-12');

--MySQL native query
insert into additional_user_details (is_active, resident_status, user_role,no_of_ppl) values (1, 'OWNER', 'SUPER_ADMIN', 0);
--insert into owner_details (owner_firstname, owner_lastname, owner_email_addr,owner_phone_nbr,owner_addr) values ("","","","","");
insert into resident_users (additional_user_details_system_addl_user_id, email_addr, firstname, flat_number, lastname, password, pending_amount, phone_nbr, residing_since, creation_date, updation_date, gender, date_of_birth) values (1, 'svlakeview560100@gmail.com', 'SV', 0, 'Lakeview', 'a0ba399c52052bb201c68e45925f57', 0.0, '000', '2016-12-12', NOW(), NOW(), 'MALE', '2016-12-12');