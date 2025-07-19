package com.personal.job_scheduler.models.dto;

import com.personal.job_scheduler.annotation.ValidJobRequests;
import com.personal.job_scheduler.models.entity.JobType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@ValidJobRequests
public record JobCreateRequest(String name, JobType jobType, String payload, String cronExpression,
                               LocalDateTime scheduledTime) {
}
