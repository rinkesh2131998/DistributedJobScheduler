-- patch-001-add-unique-constraint.sql

-- Add unique constraint to the job name
ALTER TABLE scheduler.job
    ADD CONSTRAINT uq_job_name UNIQUE (name);
