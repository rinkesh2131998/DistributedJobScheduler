package com.personal.job_scheduler.models.dto;

import com.personal.job_scheduler.models.entity.enums.JobStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record JobRunHistoryResponse(UUID id, UUID jobId, String jobName, int attemptNumber, LocalDateTime startedAt,
                                    LocalDateTime pickedAt, LocalDateTime finishedAt, JobStatus status, String result,
                                    String errorMessage) {
}
