package com.personal.job_scheduler.service.jobs;

import com.personal.job_scheduler.models.dto.JobResponse;

import java.util.UUID;

public interface JobManagement {

    JobResponse runManualJob(UUID jobId);

    JobResponse resetJobRetry(UUID jobId, boolean runNow);

}
