package com.personal.job_scheduler.service.jobs;


import com.personal.job_scheduler.models.dto.JobCreateRequest;
import com.personal.job_scheduler.models.dto.JobResponse;
import com.personal.job_scheduler.models.dto.JobUpdateRequest;
import com.personal.job_scheduler.models.entity.enums.JobType;

import java.util.List;
import java.util.UUID;

public interface JobCrudService {
    JobResponse createJob(JobCreateRequest jobCreateRequest);

    List<JobResponse> getAllJobsByType(JobType jobType);

    List<JobResponse> getAllJobs();

    JobResponse getJobDetails(UUID jobId);

    JobResponse updateJob(JobUpdateRequest jobUpdateRequest);

    JobResponse deleteJob(UUID jobId);
}
