package com.personal.job_scheduler.models.dto;

import com.personal.job_scheduler.models.entity.enums.JobActionType;
import com.personal.job_scheduler.models.entity.enums.JobStatus;
import com.personal.job_scheduler.models.entity.enums.JobType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record JobResponse(UUID id, String name, JobType jobType, JobStatus jobStatus, JobActionType jobActionType,
                          String cronExpression, LocalDateTime scheduledTime, String payload,
                          LocalDateTime createdAt, LocalDateTime updatedAt, int retryCount, int maxRetries,
                          long retryDelayMillis, LocalDateTime lastRetryAt) {
}
