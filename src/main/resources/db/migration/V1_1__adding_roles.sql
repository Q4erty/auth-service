INSERT INTO s_auth.t_role (c_authority)
VALUES ('ROLE_CLIENT'),
       ('ROLE_ADMIN'),
       ('ROLE_FREELANCER')
ON CONFLICT (c_authority) DO NOTHING;