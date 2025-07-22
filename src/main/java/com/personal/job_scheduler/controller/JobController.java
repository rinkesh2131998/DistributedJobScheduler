package com.personal.job_scheduler.controller;

import com.personal.job_scheduler.models.dto.JobCreateRequest;
import com.personal.job_scheduler.models.dto.JobResponse;
import com.personal.job_scheduler.models.dto.JobRunHistoryResponse;
import com.personal.job_scheduler.models.dto.JobUpdateRequest;
import com.personal.job_scheduler.models.entity.enums.JobType;
import com.personal.job_scheduler.service.history.JobRunHistoryService;
import com.personal.job_scheduler.service.jobs.JobCrudService;
import com.personal.job_scheduler.service.jobs.JobManagement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobManagement jobManagement;
    private final JobCrudService jobCrudService;
    private final JobRunHistoryService jobRunHistoryService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(@RequestBody @Valid JobCreateRequest jobCreateRequest) {
        JobResponse jobResponse = jobCrudService.createJob(jobCreateRequest);
        return ResponseEntity.status(201).body(jobResponse);
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<JobResponse> jobResponses = jobCrudService.getAllJobs();
        return ResponseEntity.ok(jobResponses);
    }

    @GetMapping("type")
    public ResponseEntity<List<JobResponse>> getAllJobsByType(@RequestParam JobType jobType) {
        List<JobResponse> jobResponses = jobCrudService.getAllJobsByType(jobType);
        return ResponseEntity.ok(jobResponses);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJobDetails(@PathVariable UUID jobId) {
        JobResponse jobResponse = jobCrudService.getJobDetails(jobId);
        return ResponseEntity.ok(jobResponse);
    }

    @PutMapping
    public ResponseEntity<JobResponse> updateJob(@RequestBody @Valid JobUpdateRequest jobUpdateRequest) {
        JobResponse jobResponse = jobCrudService.updateJob(jobUpdateRequest);
        return ResponseEntity.ok(jobResponse);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<JobResponse> deleteJob(@PathVariable UUID jobId) {
        JobResponse jobResponse = jobCrudService.deleteJob(jobId);
        return ResponseEntity.ok(jobResponse);
    }

    @PostMapping("/{jobId}/run")
    public ResponseEntity<JobResponse> runManualJob(@PathVariable UUID jobId) {
        JobResponse jobResponse = jobManagement.runManualJob(jobId);
        return ResponseEntity.ok(jobResponse);
    }

    @PostMapping("/{jobId}/reset")
    public ResponseEntity<JobResponse> resetRetryForFailedJobs(@PathVariable UUID jobId, @RequestParam(required = false, defaultValue = "false") boolean runNow) {
        final JobResponse jobResponse = jobManagement.resetJobRetry(jobId, runNow);
        return ResponseEntity.ok(jobResponse);
    }

    @GetMapping("/{jobId}/history")
    public ResponseEntity<Page<JobRunHistoryResponse>> getRunHistoryByJobId(@PathVariable UUID jobId,
                                                                            @RequestParam(required = false, defaultValue = "0") Integer page,
                                                                            @RequestParam(required = false, defaultValue = "10") Integer size,
                                                                            @RequestParam(defaultValue = "startedAt") String sortBy,
                                                                            @RequestParam(defaultValue = "desc") String sortDir) {
        final Sort sortDirection = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        final Pageable pageable = PageRequest.of(page, size, sortDirection);
        return ResponseEntity.ok(jobRunHistoryService.getRunHistoryByJobId(jobId, pageable));
    }

    @GetMapping("/history/{historyId}")
    public ResponseEntity<JobRunHistoryResponse> getRunHistoryByJobIdAndHistoryId(
            @PathVariable UUID historyId) {
        return ResponseEntity.ok(jobRunHistoryService.getRunHistoryByHistoryId(historyId));
    }
}
