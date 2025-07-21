-- Create the trigger function
CREATE OR REPLACE FUNCTION scheduler.set_updated_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger (only if it doesn't exist)
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1
                       FROM pg_trigger
                       WHERE tgname = 'set_updated_at_job') THEN
            CREATE TRIGGER set_updated_at_job
                BEFORE UPDATE
                ON scheduler.job
                FOR EACH ROW
            EXECUTE FUNCTION scheduler.set_updated_at();
        END IF;
    END
$$;
