package com.personal.job_scheduler.service.jobs;

import com.personal.job_scheduler.models.dto.JobExecutionSummaryResponse;
import com.personal.job_scheduler.models.dto.JobRetryResponse;
import com.personal.job_scheduler.models.dto.JobStatusCountResponse;
import com.personal.job_scheduler.models.entity.enums.JobActionType;
import com.personal.job_scheduler.models.entity.enums.JobStatus;

import java.util.List;
import java.util.UUID;

public interface JobMonitoringService {

    List<JobStatusCountResponse> countJobByStatus();

    JobStatusCountResponse countJobByStatus(JobStatus jobStatus);

    JobExecutionSummaryResponse getJobExecutionSummary(JobActionType jobActionType);

    JobRetryResponse getJobRetryStatus(UUID jobId);
}
