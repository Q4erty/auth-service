CREATE SCHEMA IF NOT EXISTS s_auth;

CREATE TABLE IF NOT EXISTS s_auth.t_client
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

CREATE TABLE IF NOT EXISTS s_auth.t_client_role
(
    id_client INT NOT NULL REFERENCES s_auth.t_client (id) ON DELETE CASCADE,
    id_role   INT NOT NULL REFERENCES s_auth.t_role (id) ON DELETE CASCADE,
    PRIMARY KEY (id_client, id_role)
);