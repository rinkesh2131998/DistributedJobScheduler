ALTER TABLE scheduler.job
    ADD COLUMN retry_count        INTEGER DEFAULT 0,
    ADD COLUMN max_retries        INTEGER DEFAULT 1,
    ADD COLUMN retry_delay_millis BIGINT  DEFAULT 1000,
    ADD COLUMN last_retry_at      TIMESTAMP;
