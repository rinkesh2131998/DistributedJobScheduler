package com.personal.job_scheduler.service.executor;

import com.personal.job_scheduler.exception.JobHandlerException;
import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.enums.JobActionType;
import com.personal.job_scheduler.models.entity.enums.JobStatus;
import com.personal.job_scheduler.repository.JobRepository;
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
    private final JobRepository jobRepository;
    private final JobHandlerRegistry jobHandlerRegistry;

    public void submit(final Job job) {
        executorService.submit(() -> {
            try {
                handleJobWithRetries(job, getJobhandler(job.getJobActionType()));
            } catch (final Exception e) {
                log.error("Unrecoverable error while executing job: {}, cause: {} ", job.getId(), e.getMessage());
            } finally {
                jobRepository.save(job);
            }
        });
    }

    private void handleJobWithRetries(final Job job, final JobHandler jobhandler) {
        final int maxRetries = job.getMaxRetries();
        final int retryCount = job.getRetryCount();
        if (retryCount < maxRetries) {
            try {
                jobhandler.execute(job);
            } catch (final Exception exception) {
                if (retryCount + 1 == maxRetries) {
                    job.setJobStatus(JobStatus.FAILED);
                } else {
                    job.setJobStatus(JobStatus.SCHEDULED);
                }
                job.setRetryCount(retryCount + 1);
                job.setLastRetryAt(LocalDateTime.now());
                job.setResult("Retrying due to error: " + exception.getMessage());
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

    private void handleJob(Job job) {
        final JobActionType jobActionType = job.getJobActionType();
        final JobHandler jobHandler = jobHandlerRegistry.getJobHandler(jobActionType);
        if (jobHandler == null) {
            throw new JobHandlerException("No JobHandler found for type: " + jobActionType);
        }
        jobHandler.execute(job);
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
