DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'jobtype') THEN
            CREATE TYPE jobtype AS ENUM ('CRON', 'ONE_TIME', 'MANUAL');
        END IF;

        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'jobstatus') THEN
            CREATE TYPE jobstatus AS ENUM ('SCHEDULED', 'RUNNING', 'SUCCESS', 'FAILED', 'CANCELLED');
        END IF;
    END
$$;
