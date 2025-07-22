package com.personal.job_scheduler.repository;

import com.personal.job_scheduler.models.entity.JobRunHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobHistoryRepository extends JpaRepository<JobRunHistory, UUID> {

    Page<JobRunHistory> findByJobId(UUID jobId, Pageable pageable);

    @Query(nativeQuery = true, value = """
            SELECT j.job_action_type AS jobActionType,
                   COUNT(*) AS executionCount,
                   AVG(EXTRACT(EPOCH FROM (h.finished_at - h.started_at)) * 1000) AS avgJobDurationMs,
                   MIN(EXTRACT(EPOCH FROM (h.finished_at - h.started_at)) * 1000) AS minDurationMs,
                   MAX(EXTRACT(EPOCH FROM (h.finished_at - h.started_at)) * 1000) AS maxDurationMs
            FROM scheduler.job_run_history h
            JOIN scheduler.job j ON h.job_id = j.id
            WHERE j.job_action_type = :jobActionType
              AND h.status = 'SUCCESS'
              AND h.started_at IS NOT NULL AND h.finished_at IS NOT NULL
            GROUP BY j.job_action_type
            """)
    Object getJobExecutionSummary(@Param("jobActionType") String jobActionType);

    @Query(nativeQuery = true, value = """
            SELECT
              j.id AS jobId,
              j.name AS jobName,
              j.retry_count AS retryCount,
              j.max_retries AS maxRetries,
              COUNT(h.id) AS totalAttempts,
              latest_h.error_message AS errorMessage
            FROM scheduler.job j
            LEFT JOIN scheduler.job_run_history h ON j.id = h.job_id
            LEFT JOIN LATERAL (
                SELECT h2.error_message
                FROM scheduler.job_run_history h2
                WHERE h2.job_id = j.id
                ORDER BY h2.finished_at DESC
                LIMIT 1
            ) latest_h ON true
            WHERE j.id = :jobId
            GROUP BY j.id, j.name, j.retry_count, j.max_retries, latest_h.error_message
                        """)
    Object getJobRetryStatus(@Param("jobId") UUID jobId);

}
