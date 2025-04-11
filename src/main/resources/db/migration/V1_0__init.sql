CREATE SCHEMA IF NOT EXISTS s_auth;

CREATE TABLE IF NOT EXISTS s_auth.t_user
(
    id            SERIAL PRIMARY KEY,
    c_username    VARCHAR NOT NULL UNIQUE,
    c_email       VARCHAR NOT NULL UNIQUE,
    c_password    TEXT    NOT NULL,
    c_is_verified BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS s_auth.t_role
(
    id          SERIAL PRIMARY KEY,
    c_authority VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS s_auth.t_user_role
(
    id_user INT NOT NULL REFERENCES s_auth.t_user (id) ON DELETE CASCADE,
    id_role INT NOT NULL REFERENCES s_auth.t_role (id) ON DELETE CASCADE,
    PRIMARY KEY (id_user, id_role)
);