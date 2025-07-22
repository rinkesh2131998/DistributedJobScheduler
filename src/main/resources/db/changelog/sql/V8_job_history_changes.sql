ALTER TABLE scheduler.job
    DROP COLUMN result;

CREATE TABLE scheduler.job_run_history
(
    id             UUID PRIMARY KEY,
    job_id         UUID    NOT NULL,
    attempt_number INTEGER NOT NULL,
    picked_at      TIMESTAMP,
    started_at     TIMESTAMP,
    finished_at    TIMESTAMP,
    status         VARCHAR,
    result         oid,
    error_message  oid,

    CONSTRAINT fk_job_history_job FOREIGN KEY (job_id)
        REFERENCES scheduler.job (id) ON DELETE CASCADE
);
