package com.personal.job_scheduler.models.dto;

import com.personal.job_scheduler.models.entity.enums.JobActionType;
import lombok.Builder;

@Builder
public record JobExecutionSummaryResponse(JobActionType jobActionType, long avgJobDurationMs, long minDurationMs,
                                          long maxDurationMs, long executionCount) {
}
