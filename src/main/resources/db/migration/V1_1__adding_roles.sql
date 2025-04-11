INSERT INTO s_auth.t_role (c_authority)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN')
ON CONFLICT (c_authority) DO NOTHING;