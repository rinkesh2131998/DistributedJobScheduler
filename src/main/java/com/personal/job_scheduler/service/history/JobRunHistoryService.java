package com.personal.job_scheduler.service.history;

import com.personal.job_scheduler.models.dto.JobRunHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface JobRunHistoryService {

    JobRunHistoryResponse getRunHistoryByHistoryId(UUID historyId);

    Page<JobRunHistoryResponse> getRunHistoryByJobId(UUID jobId, Pageable pageable);
}
