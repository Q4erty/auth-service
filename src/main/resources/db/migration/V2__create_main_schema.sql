CREATE TABLE s_auth.t_category
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE s_auth.t_order
(
    id            SERIAL PRIMARY KEY,
    title         VARCHAR(255)   NOT NULL,
    description   TEXT           NOT NULL,
    price         NUMERIC(15, 2) NOT NULL,
    deadline      DATE           NOT NULL,
    category_id   INT REFERENCES s_auth.t_category (id),
    client_id     INT REFERENCES s_auth.t_client (id),
    freelancer_id INT REFERENCES s_auth.t_client (id)
);

CREATE TABLE s_auth.t_application
(
    id            SERIAL PRIMARY KEY,
    order_id      INT REFERENCES s_auth.t_order (id),
    freelancer_id INT REFERENCES s_auth.t_client (id),
    status        VARCHAR(50) NOT NULL,
    proposal      TEXT,
    created_at    TIMESTAMP   NOT NULL
);