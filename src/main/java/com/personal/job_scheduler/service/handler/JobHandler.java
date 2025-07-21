package com.personal.job_scheduler.service.handler;

import com.personal.job_scheduler.models.entity.Job;

public interface JobHandler {
    boolean canHandle(Job job);

    void execute(Job job);
}
