package com.personal.job_scheduler.service.jobs.handlers;

import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.enums.JobActionType;
import com.personal.job_scheduler.service.jobs.JobHandler;

public class HttpJobHandler implements JobHandler {
    @Override
    public boolean canHandle(Job job) {
        return job.getJobActionType() == JobActionType.HTTP;
    }

    @Override
    public void execute(Job job) {

    }
}
