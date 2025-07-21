package com.personal.job_scheduler.controller;

import com.personal.job_scheduler.models.dto.JobCreateRequest;
import com.personal.job_scheduler.models.dto.JobResponse;
import com.personal.job_scheduler.models.dto.JobUpdateRequest;
import com.personal.job_scheduler.models.entity.enums.JobType;
import com.personal.job_scheduler.service.management.JobManagement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class JobManagementController {
    private final JobManagement jobManagement;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(@RequestBody @Valid JobCreateRequest jobCreateRequest) {
        JobResponse jobResponse = jobManagement.createJob(jobCreateRequest);
        return ResponseEntity.status(201).body(jobResponse);
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<JobResponse> jobResponses = jobManagement.getAllJobs();
        return ResponseEntity.ok(jobResponses);
    }

    @GetMapping("type")
    public ResponseEntity<List<JobResponse>> getAllJobsByType(@RequestParam JobType jobType) {
        List<JobResponse> jobResponses = jobManagement.getAllJobsByType(jobType);
        return ResponseEntity.ok(jobResponses);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJobDetails(@PathVariable UUID jobId) {
        JobResponse jobResponse = jobManagement.getJobDetails(jobId);
        return ResponseEntity.ok(jobResponse);
    }

    @PutMapping
    public ResponseEntity<JobResponse> updateJob(@RequestBody @Valid JobUpdateRequest jobUpdateRequest) {
        JobResponse jobResponse = jobManagement.updateJob(jobUpdateRequest);
        return ResponseEntity.ok(jobResponse);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<JobResponse> deleteJob(@PathVariable UUID jobId) {
        JobResponse jobResponse = jobManagement.deleteJob(jobId);
        return ResponseEntity.ok(jobResponse);
    }

    @GetMapping("/{jobId}/run")
    public ResponseEntity<JobResponse> runManualJob(@PathVariable UUID jobId) {
        JobResponse jobResponse = jobManagement.runManualJob(jobId);
        return ResponseEntity.ok(jobResponse);
    }
}
