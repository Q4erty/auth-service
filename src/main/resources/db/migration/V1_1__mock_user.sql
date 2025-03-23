INSERT INTO s_auth.t_user(id, c_username, c_password)
VALUES (1, 'dbuser', '{noop}password');

INSERT INTO s_auth.t_user_authority(id_user, c_authority)
VALUES (1, 'ROLE_DB_USER'), (1, 'ROLE_USER');

