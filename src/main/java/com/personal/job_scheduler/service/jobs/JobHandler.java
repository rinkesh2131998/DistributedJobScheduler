package com.personal.job_scheduler.service.jobs;

import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.JobRunHistory;

public interface JobHandler {
    boolean canHandle(Job job);

    //todo: the current is logic is very basic will improve firther after completing the other parts
    void execute(Job job, JobRunHistory jobRunHistory);
}
