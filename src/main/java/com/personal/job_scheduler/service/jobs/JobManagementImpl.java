package com.personal.job_scheduler.service.jobs;

import com.personal.job_scheduler.exception.ResourceNotFoundException;
import com.personal.job_scheduler.models.dto.JobResponse;
import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.enums.JobStatus;
import com.personal.job_scheduler.models.entity.enums.JobType;
import com.personal.job_scheduler.repository.JobRepository;
import com.personal.job_scheduler.service.executor.JobExecutor;
import com.personal.job_scheduler.util.CommonUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class JobManagementImpl implements JobManagement {
    private final JobRepository jobRepository;
    private final JobExecutor jobExecutor;

    @Override
    public JobResponse runManualJob(UUID jobId) {
        final Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        if (job.getType() != JobType.MANUAL) {
            throw new IllegalArgumentException("Job with id: " + jobId + " is not a manual job");
        }
        if (job.getJobStatus() == JobStatus.RUNNING) {
            throw new IllegalStateException("Job with id: " + jobId + " is in RUNNING state");
        }
        job.setJobStatus(JobStatus.RUNNING);
        job.setPickedAt(LocalDateTime.now());
        final Job updatedJob = jobRepository.save(job);
        jobExecutor.submit(updatedJob);
        return CommonUtils.mapJobToResponseDto(updatedJob);
    }

    @Override
    public JobResponse resetJobRetry(UUID jobId, boolean runNow) {
        final Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        job.setJobStatus(JobStatus.SCHEDULED);
        job.setRetryCount(0);
        job.setLastRetryAt(null);
        final Job updatedJob = jobRepository.save(job);
        if (runNow) {
            jobExecutor.submit(updatedJob);
        }
        return CommonUtils.mapJobToResponseDto(updatedJob);
    }
}
