CREATE TABLE IF NOT EXISTS scheduler.job
(
    id              UUID PRIMARY KEY            DEFAULT uuid_generate_v4(),
    name            TEXT    NOT NULL,
    type            VARCHAR NOT NULL,
    job_status      VARCHAR NOT NULL,
    job_action_type VARCHAR NOT NULL,
    payload         oid,
    result          oid,
    created_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
