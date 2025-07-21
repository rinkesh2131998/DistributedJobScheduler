package com.personal.job_scheduler.models.dto;

import com.personal.job_scheduler.annotation.ValidJobRequests;
import com.personal.job_scheduler.models.entity.enums.JobActionType;
import com.personal.job_scheduler.models.entity.enums.JobType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@ValidJobRequests
public record JobUpdateRequest(@NotNull UUID id, @NotNull String name, @NotNull JobType jobType,
                               @NotNull JobActionType jobActionType, String payload, String cronExpression,
                               LocalDateTime scheduledTime) {
}
