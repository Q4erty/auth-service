CREATE SCHEMA IF NOT EXISTS s_auth;

CREATE TABLE IF NOT EXISTS s_auth.t_user
(
    id         INT PRIMARY KEY,
    c_username VARCHAR NOT NULL UNIQUE,
    c_password TEXT
);

CREATE TABLE IF NOT EXISTS s_auth.t_user_authority
(
    id          SERIAL PRIMARY KEY,
    id_user     INT     NOT NULL REFERENCES t_user (id),
    c_authority VARCHAR NOT NULL
);

