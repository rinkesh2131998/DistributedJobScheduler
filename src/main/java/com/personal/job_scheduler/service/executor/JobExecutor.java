package com.personal.job_scheduler.service.executor;

import com.personal.job_scheduler.exception.JobHandlerException;
import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.JobRunHistory;
import com.personal.job_scheduler.models.entity.enums.JobActionType;
import com.personal.job_scheduler.models.entity.enums.JobStatus;
import com.personal.job_scheduler.service.jobs.JobHandler;
import com.personal.job_scheduler.service.jobs.JobHandlerRegistry;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobExecutor {

    private final ExecutorService executorService;
    private final JobPersistenceService jobPersistenceService;
    private final JobHandlerRegistry jobHandlerRegistry;

    public void submit(final Job job) {
        final JobRunHistory jobRunHistory = new JobRunHistory();
        executorService.submit(() -> {
            try {
                handleJobWithRetries(job, getJobhandler(job.getJobActionType()), jobRunHistory);
                jobPersistenceService.saveJobAndRunHistoryOnTransaction(job, jobRunHistory);
            } catch (final Exception e) {
                log.error("Unrecoverable error while executing job: {}, cause: {} ", job.getId(), e.getMessage());
                jobPersistenceService.saveJobAndRunHistoryOnTransaction(job, jobRunHistory);
            }
        });
    }

    private void handleJobWithRetries(final Job job, final JobHandler jobhandler, JobRunHistory jobRunHistory) {
        final int maxRetries = job.getMaxRetries();
        final int retryCount = job.getRetryCount();
        if (retryCount < maxRetries) {
            try {
                jobRunHistory.setJob(job);
                jobRunHistory.setStartedAt(LocalDateTime.now());
                jobRunHistory.setPickedAt(LocalDateTime.now());
                jobRunHistory.setAttemptNumber(retryCount + 1);
                jobhandler.execute(job, jobRunHistory);
                jobRunHistory.setFinishedAt(LocalDateTime.now());
                jobRunHistory.setStatus(JobStatus.SUCCESS);
                job.setJobStatus(JobStatus.SUCCESS);
            } catch (final Exception exception) {
                jobRunHistory.setFinishedAt(LocalDateTime.now());
                if (retryCount + 1 == maxRetries) {
                    job.setJobStatus(JobStatus.FAILED);
                    jobRunHistory.setStatus(JobStatus.FAILED);
                } else {
                    job.setJobStatus(JobStatus.SCHEDULED);
                    jobRunHistory.setStatus(JobStatus.SCHEDULED);
                }
                jobRunHistory.setErrorMessage(exception.getMessage());
                jobRunHistory.setResult("Retrying due to error: " + exception.getMessage());
                job.setRetryCount(retryCount + 1);
                job.setLastRetryAt(LocalDateTime.now());
                log.error("Job: {}, failed cause: {}", job.getId(), exception.getMessage());
            }
        }
    }

    private JobHandler getJobhandler(final JobActionType jobActionType) {
        final JobHandler jobHandler = jobHandlerRegistry.getJobHandler(jobActionType);
        if (jobHandler == null) {
            throw new JobHandlerException("No JobHandler found for type: " + jobActionType);
        }
        return jobHandler;
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
