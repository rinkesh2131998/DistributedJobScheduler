package com.personal.job_scheduler.models.dto;

import com.personal.job_scheduler.models.entity.enums.JobStatus;
import lombok.Builder;

@Builder
public record JobStatusCountResponse(JobStatus jobStatus, int count) {
    
}
