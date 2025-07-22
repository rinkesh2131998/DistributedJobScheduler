package com.personal.job_scheduler.service.history;

import com.personal.job_scheduler.models.dto.JobRunHistoryResponse;
import com.personal.job_scheduler.models.entity.JobRunHistory;
import com.personal.job_scheduler.repository.JobHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobRunHistoryServiceImpl implements JobRunHistoryService {
    private final JobHistoryRepository jobHistoryRepository;

    @Override
    public JobRunHistoryResponse getRunHistoryByHistoryId(UUID historyId) {
        JobRunHistory history = jobHistoryRepository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("Job run history not found for id: " + historyId));
        return mapTojobRunHistoryResponse(history);
    }

    @Override
    public Page<JobRunHistoryResponse> getRunHistoryByJobId(UUID jobId, Pageable pageable) {
        return jobHistoryRepository.findByJobId(jobId, pageable)
                .map(this::mapTojobRunHistoryResponse);
    }

    private JobRunHistoryResponse mapTojobRunHistoryResponse(JobRunHistory history) {
        return JobRunHistoryResponse.builder()
                .id(history.getId())
                .jobId(history.getJob().getId())
                .jobName(history.getJob().getName())
                .attemptNumber(history.getAttemptNumber())
                .startedAt(history.getStartedAt())
                .pickedAt(history.getPickedAt())
                .finishedAt(history.getFinishedAt())
                .status(history.getStatus())
                .result(history.getResult())
                .errorMessage(history.getErrorMessage())
                .build();
    }
}
