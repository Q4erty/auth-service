ALTER TABLE s_auth.t_application
    ALTER COLUMN status TYPE INTEGER
        USING CASE status
                  WHEN 'PENDING' THEN 0
                  WHEN 'ACCEPTED' THEN 1
                  WHEN 'CANCELLED' THEN 2
                  WHEN 'DECLINED' THEN 3
        END;