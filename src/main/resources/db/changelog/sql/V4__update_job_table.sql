ALTER TABLE scheduler.job
    ADD COLUMN cron_expression TEXT,
    ADD COLUMN scheduled_time  TIMESTAMP WITHOUT TIME ZONE;
