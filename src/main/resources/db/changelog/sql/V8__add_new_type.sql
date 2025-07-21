DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'jobActionType') THEN
            CREATE TYPE jobtype AS ENUM ('HTTP', 'SLEEP', 'ECHO');
        END IF;
    END
$$;
