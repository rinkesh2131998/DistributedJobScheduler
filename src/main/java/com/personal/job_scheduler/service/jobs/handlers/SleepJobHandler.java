package com.personal.job_scheduler.service.jobs.handlers;

import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.JobRunHistory;
import com.personal.job_scheduler.models.entity.enums.JobActionType;
import com.personal.job_scheduler.service.jobs.JobHandler;

public class SleepJobHandler implements JobHandler {
    @Override
    public boolean canHandle(Job job) {
        return job.getJobActionType() == JobActionType.SLEEP;
    }

    @Override
    public void execute(Job job, JobRunHistory jobRunHistory) {
        try {
            if ("FAIL_THIS_JOB".equals(job.getPayload())) {
                throw new IllegalStateException("Forced failure for retry test");
            }
            long millis = Long.parseLong(job.getPayload());
            Thread.sleep(millis);
            jobRunHistory.setResult("Slept for " + millis + " ms");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid sleep duration: " + job.getPayload(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep interrupted", e);
        }
    }
}
