ALTER TABLE s_auth.t_application
    ALTER COLUMN status TYPE smallint
        USING CASE status
                  WHEN 'PENDING' THEN 0
                  WHEN 'ACCEPTED' THEN 1
                  WHEN 'CANCELLED' THEN 2
        END;