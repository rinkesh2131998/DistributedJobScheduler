package com.personal.job_scheduler.service.jobs;

import com.personal.job_scheduler.exception.ResourceNotFoundException;
import com.personal.job_scheduler.models.dto.JobCreateRequest;
import com.personal.job_scheduler.models.dto.JobResponse;
import com.personal.job_scheduler.models.dto.JobUpdateRequest;
import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.enums.JobStatus;
import com.personal.job_scheduler.models.entity.enums.JobType;
import com.personal.job_scheduler.repository.JobRepository;
import com.personal.job_scheduler.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobCrudServiceImpl implements JobCrudService {
    private static final int DEFAULT_MAX_RETRIES = 3;
    private static final long DEFAULT_DELAY_RETRY_MILLIS = 1000;

    private final JobRepository jobRepository;

    @Override
    public JobResponse createJob(final JobCreateRequest jobCreateRequest) {

        final int maxRetries = jobCreateRequest.maxRetries() < 0 ? DEFAULT_MAX_RETRIES : jobCreateRequest.maxRetries();
        final long retryDelayMillis = jobCreateRequest.retryDelayMillis() < 0 ? DEFAULT_DELAY_RETRY_MILLIS : jobCreateRequest.retryDelayMillis();

        final Job job = Job.builder()
                .name(jobCreateRequest.name())
                .type(jobCreateRequest.jobType())
                .jobStatus(JobStatus.SCHEDULED) // initial state would always be scheduled for new job
                .cronExpression(jobCreateRequest.cronExpression())
                .scheduledTime(jobCreateRequest.scheduledTime())
                .payload(jobCreateRequest.payload())
                .jobActionType(jobCreateRequest.jobActionType())
                .maxRetries(maxRetries)
                .retryDelayMillis(retryDelayMillis)
                .build();
        final Job jobResponse = jobRepository.save(job);
        return CommonUtils.mapJobToResponseDto(jobResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobsByType(final JobType jobType) {
        return jobRepository.findByType(jobType)
                .stream()
                .map(CommonUtils::mapJobToResponseDto)
                .toList();
    }

    @Override
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(CommonUtils::mapJobToResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse getJobDetails(final UUID jobId) {
        return jobRepository.findById(jobId)
                .map(CommonUtils::mapJobToResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
    }


    @Override
    public JobResponse updateJob(final JobUpdateRequest jobUpdateRequest) {
        if (jobUpdateRequest.id() == null) {
            throw new IllegalArgumentException("Job ID must not be null for update operation");
        }
        final Job existingJob = jobRepository.findById(jobUpdateRequest.id())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobUpdateRequest.id()));

        existingJob.setName(jobUpdateRequest.name());
        existingJob.setType(jobUpdateRequest.jobType());
        existingJob.setCronExpression(jobUpdateRequest.cronExpression());
        existingJob.setScheduledTime(jobUpdateRequest.scheduledTime());
        existingJob.setJobActionType(jobUpdateRequest.jobActionType());
        existingJob.setPayload(jobUpdateRequest.payload());
        existingJob.setMaxRetries(jobUpdateRequest.maxRetries());
        existingJob.setRetryDelayMillis(jobUpdateRequest.retryDelayMillis());
        final Job updatedJob = jobRepository.save(existingJob);
        return CommonUtils.mapJobToResponseDto(updatedJob);
    }

    @Override
    public JobResponse deleteJob(final UUID jobId) {
        final Job existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        jobRepository.delete(existingJob);
        return CommonUtils.mapJobToResponseDto(existingJob);
    }
}
