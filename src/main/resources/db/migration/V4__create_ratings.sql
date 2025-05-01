CREATE TABLE s_auth.t_rating
(
    id           SERIAL PRIMARY KEY,
    score        INT       NOT NULL CHECK (score BETWEEN 1 AND 5),
    comment      TEXT,
    order_id     INT       NOT NULL REFERENCES s_auth.t_order (id),
    from_user_id INT       NOT NULL REFERENCES s_auth.t_client (id),
    to_user_id   INT       NOT NULL REFERENCES s_auth.t_client (id),
    created_at   TIMESTAMP NOT NULL
);

ALTER TABLE s_auth.t_client
    ADD COLUMN average_rating NUMERIC(3,2) DEFAULT 0.00,
    ADD COLUMN rating_count INT DEFAULT 0;
