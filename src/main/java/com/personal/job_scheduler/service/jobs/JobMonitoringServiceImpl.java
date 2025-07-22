package com.personal.job_scheduler.service.jobs;

import com.personal.job_scheduler.models.dto.JobExecutionSummaryResponse;
import com.personal.job_scheduler.models.dto.JobRetryResponse;
import com.personal.job_scheduler.models.dto.JobStatusCountResponse;
import com.personal.job_scheduler.models.entity.enums.JobActionType;
import com.personal.job_scheduler.models.entity.enums.JobStatus;
import com.personal.job_scheduler.repository.JobHistoryRepository;
import com.personal.job_scheduler.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobMonitoringServiceImpl implements JobMonitoringService {
    private final JobHistoryRepository jobHistoryRepository;
    private final JobRepository jobRepository;

    @Override
    public List<JobStatusCountResponse> countJobByStatus() {
        final List<JobStatus> jobStatuses = List.of(JobStatus.SCHEDULED, JobStatus.RUNNING, JobStatus.SUCCESS,
                JobStatus.FAILED, JobStatus.CANCELLED);
        return jobStatuses.stream()
                .map(this::countJobByStatus)
                .toList();
    }

    @Override
    public JobStatusCountResponse countJobByStatus(JobStatus jobStatus) {
        final int countByStatus = jobRepository.countByJobStatus(jobStatus);
        return createJobStatusCountResponse(jobStatus, countByStatus);
    }

    private JobStatusCountResponse createJobStatusCountResponse(JobStatus jobStatus, int count) {
        return JobStatusCountResponse.builder()
                .jobStatus(jobStatus)
                .count(count)
                .build();
    }

    @Override
    public JobExecutionSummaryResponse getJobExecutionSummary(JobActionType jobActionType) {
        final Object row = jobHistoryRepository.getJobExecutionSummary(jobActionType.name());
        if (row == null) {
            log.warn("No execution summary found for job action type: {}", jobActionType);
            throw new IllegalArgumentException("No execution summary found for job action type: " + jobActionType);
        }
        final Object[] jobExecutionSummary = (Object[]) row;
        return JobExecutionSummaryResponse.builder()
                .jobActionType(jobActionType)
                .executionCount(((Number) jobExecutionSummary[1]).longValue())
                .avgJobDurationMs(((Number) jobExecutionSummary[2]).longValue())
                .minDurationMs(((Number) jobExecutionSummary[3]).longValue())
                .maxDurationMs(((Number) jobExecutionSummary[4]).longValue())
                .build();

    }

    @Override
    public JobRetryResponse getJobRetryStatus(UUID jobId) {
        final Object row = jobHistoryRepository.getJobRetryStatus(jobId);
        if (row == null) {
            log.warn("No retry status found for job ID: {}", jobId);
            throw new IllegalArgumentException("No retry status found for job ID: " + jobId);
        }
        final Object[] jobRetryStatus = (Object[]) row;
        return JobRetryResponse.builder()
                .jobId(jobId)
                .jobName(((String) jobRetryStatus[1]))
                .retryCount(((Number) jobRetryStatus[2]).intValue())
                .maxRetries(((Number) jobRetryStatus[3]).intValue())
                .totalAttempts(jobRetryStatus[5] == null ? 0 : ((Number) jobRetryStatus[5]).intValue())
                .build();
    }
}
