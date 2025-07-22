package com.personal.job_scheduler.controller;

import com.personal.job_scheduler.models.dto.JobExecutionSummaryResponse;
import com.personal.job_scheduler.models.dto.JobRetryResponse;
import com.personal.job_scheduler.models.dto.JobStatusCountResponse;
import com.personal.job_scheduler.models.entity.enums.JobActionType;
import com.personal.job_scheduler.models.entity.enums.JobStatus;
import com.personal.job_scheduler.service.jobs.JobMonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/metrics/jobs")
@ConditionalOnProperty(name = "api.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class JobMonitoringController {
    private final JobMonitoringService jobMonitoringService;

    @GetMapping("/status/count/all")
    public ResponseEntity<List<JobStatusCountResponse>> countAllJobByStatus() {
        List<JobStatusCountResponse> response = jobMonitoringService.countJobByStatus();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/count")
    public ResponseEntity<JobStatusCountResponse> countJobByStatus(@RequestParam(required = false) JobStatus jobStatus) {
        JobStatusCountResponse response = jobMonitoringService.countJobByStatus(jobStatus);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/execution/summary")
    public ResponseEntity<JobExecutionSummaryResponse> getJobExecutionSummary(@RequestParam JobActionType jobActionType) {
        JobExecutionSummaryResponse response = jobMonitoringService.getJobExecutionSummary(jobActionType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{jobId}/retry/status")
    public ResponseEntity<JobRetryResponse> getJobRetryStatus(@PathVariable String jobId) {
        UUID jobUUID = UUID.fromString(jobId);
        JobRetryResponse response = jobMonitoringService.getJobRetryStatus(jobUUID);
        return ResponseEntity.ok(response);
    }
}
