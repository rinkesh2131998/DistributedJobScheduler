package com.personal.job_scheduler.models.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record JobRetryResponse(UUID jobId, String jobName, int totalAttempts, int retryCount, int maxRetries,
                               String errorMessage) {
}
