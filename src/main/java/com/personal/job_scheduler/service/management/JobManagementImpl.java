package com.personal.job_scheduler.service.management;

import com.personal.job_scheduler.exception.ResourceNotFoundException;
import com.personal.job_scheduler.models.dto.JobCreateRequest;
import com.personal.job_scheduler.models.dto.JobResponse;
import com.personal.job_scheduler.models.dto.JobUpdateRequest;
import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.JobStatus;
import com.personal.job_scheduler.models.entity.JobType;
import com.personal.job_scheduler.repository.JobRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class JobManagementImpl implements JobManagement {

    private final JobRepository jobRepository;

    @Override
    public JobResponse createJob(final JobCreateRequest jobCreateRequest) {
        final Job job = Job.builder()
                .name(jobCreateRequest.name())
                .type(jobCreateRequest.jobType())
                .jobStatus(JobStatus.SCHEDULED) // initial state would always be scheduled for new job
                .cronExpression(jobCreateRequest.cronExpression())
                .scheduledTime(jobCreateRequest.scheduledTime())
                .payload(jobCreateRequest.payload())
                .build();
        final Job jobResponse = jobRepository.save(job);
        return toDto(jobResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponse> getAllJobsByType(final JobType jobType) {
        return jobRepository.findByType(jobType)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse getJobDetails(final UUID jobId) {
        return jobRepository.findById(jobId)
                .map(this::toDto)
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
        existingJob.setPayload(jobUpdateRequest.payload());
        final Job updatedJob = jobRepository.save(existingJob);
        return toDto(updatedJob);
    }

    @Override
    public JobResponse deleteJob(final UUID jobId) {
        final Job existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        jobRepository.delete(existingJob);
        return toDto(existingJob);
    }

    //convert Job entity to JobResponse DTO
    JobResponse toDto(final Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .name(job.getName())
                .jobType(job.getType())
                .jobStatus(job.getJobStatus())
                .cronExpression(job.getCronExpression())
                .scheduledTime(job.getScheduledTime())
                .payload(job.getPayload())
                .result(job.getResult())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}
