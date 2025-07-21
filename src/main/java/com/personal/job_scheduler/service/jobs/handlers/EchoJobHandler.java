package com.personal.job_scheduler.service.jobs.handlers;

import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.enums.JobActionType;
import com.personal.job_scheduler.models.entity.enums.JobStatus;
import com.personal.job_scheduler.service.jobs.JobHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoJobHandler implements JobHandler {
    @Override
    public boolean canHandle(Job job) {
        return job.getJobActionType() == JobActionType.ECHO;
    }

    @Override
    public void execute(Job job) {
        job.setResult(job.getPayload());
        job.setJobStatus(JobStatus.SUCCESS);
        log.info("Executed Job with Id: {}", job.getId());
    }
}
