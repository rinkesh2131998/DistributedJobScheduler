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
                //todo: simulate a job for now, not actually running the payload
                Thread.sleep(1000);
                handleJob(job);
                jobRepository.save(job);
            } catch (final Exception e) {
                job.setJobStatus(JobStatus.FAILED);
                job.setResult("Error: " + e.getMessage());
                log.warn("Failed to execute Job with Id: {} , cause: {}", job.getId(), e.getMessage());
            } finally {
                jobRepository.save(job);
            }
        });
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
