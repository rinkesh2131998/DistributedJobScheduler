package com.personal.job_scheduler.util;

import com.personal.job_scheduler.models.dto.JobResponse;
import com.personal.job_scheduler.models.entity.Job;

public class CommonUtils {

    public static JobResponse mapJobToResponseDto(final Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .name(job.getName())
                .jobType(job.getType())
                .jobStatus(job.getJobStatus())
                .cronExpression(job.getCronExpression())
                .scheduledTime(job.getScheduledTime())
                .payload(job.getPayload())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .retryCount(job.getRetryCount())
                .maxRetries(job.getMaxRetries())
                .retryDelayMillis(job.getRetryDelayMillis())
                .lastRetryAt(job.getLastRetryAt())
                .build();
    }
}
