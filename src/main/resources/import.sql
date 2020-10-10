insert into user_settings_entity (days_amount, default_city) values (3, 'Katowice');
insert into user_entity (user_name, password, settings_id) values ('admin', 'admin', 1);
insert into role (role) values ('ADMIN');
insert into role (role) values ('USER');
insert into users_role (role_id, user_id) values (1,1);