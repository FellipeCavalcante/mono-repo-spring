insert into user (id, email,first_name,last_name, roles, password) values (1, 'manu@reginato.com', 'Manu', 'Reginato', 'USER', '{bcrypt}$2a$10$iq46TcNKxkLjsoyvceoJieq8qKwGfQboSmBXkiThewVMF0PQCvPVm');
insert into user (id, email,first_name,last_name, roles, password) values (2, 'mamoru@takamura.com', 'Mamoru', 'Takamura', 'USER', '{bcrypt}$2a$10$iq46TcNKxkLjsoyvceoJieq8qKwGfQboSmBXkiThewVMF0PQCvPVm');
insert into profile (id, description, name) values (1, 'Manager everything', 'Admin');
insert into user_profile (id, user_id, profile_id) values (1, 1, 1);
insert into user_profile (id, user_id, profile_id) values (2, 2, 1);
