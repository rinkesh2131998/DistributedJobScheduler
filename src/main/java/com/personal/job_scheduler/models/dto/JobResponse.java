package com.personal.job_scheduler.models.dto;

import com.personal.job_scheduler.models.entity.JobStatus;
import com.personal.job_scheduler.models.entity.JobType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record JobResponse(UUID id, String name, JobType jobType, JobStatus jobStatus, String cronExpression,
                          LocalDateTime scheduledTime, String payload, String result, LocalDateTime createdAt,
                          LocalDateTime updatedAt) {
}
