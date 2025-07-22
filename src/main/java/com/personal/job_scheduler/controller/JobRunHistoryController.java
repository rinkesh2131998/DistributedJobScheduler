package com.personal.job_scheduler.controller;

import com.personal.job_scheduler.models.dto.JobRunHistoryResponse;
import com.personal.job_scheduler.service.history.JobRunHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/run-history")
@RequiredArgsConstructor
public class JobRunHistoryController {
    private final JobRunHistoryService jobRunHistoryService;

    @GetMapping("/job/{jobId}/history")
    public ResponseEntity<Page<JobRunHistoryResponse>> getRunHistoryByJobId(@PathVariable UUID jobId,
                                                                            @RequestParam(required = false, defaultValue = "0") Integer page,
                                                                            @RequestParam(required = false, defaultValue = "10") Integer size,
                                                                            @RequestParam(defaultValue = "startedAt") String sortBy,
                                                                            @RequestParam(defaultValue = "desc") String sortDir) {
        final Sort sortDirection = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        final Pageable pageable = PageRequest.of(page, size, sortDirection);
        return ResponseEntity.ok(jobRunHistoryService.getRunHistoryByJobId(jobId, pageable));
    }

    @GetMapping("/job/history/{historyId}")
    public ResponseEntity<JobRunHistoryResponse> getRunHistoryByJobIdAndHistoryId(
            @PathVariable UUID historyId) {
        return ResponseEntity.ok(jobRunHistoryService.getRunHistoryByHistoryId(historyId));
    }
}
