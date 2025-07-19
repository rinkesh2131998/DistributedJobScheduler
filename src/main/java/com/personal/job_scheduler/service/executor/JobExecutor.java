package com.personal.job_scheduler.service.executor;

import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.JobStatus;
import com.personal.job_scheduler.repository.JobRepository;
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

    public void submit(final Job job) {
        executorService.submit(() -> {
            try {
                //todo: simulate a job for now, not actually running the payload
                Thread.sleep(1000);
                job.setJobStatus(JobStatus.SUCCESS);
                job.setResult("Job Completed");
                log.info("Executed Job with Id: {}", job.getId());
            } catch (final Exception e) {
                job.setJobStatus(JobStatus.FAILED);
                job.setResult("Error: " + e.getMessage());
                log.warn("Failed to execute Job with Id: {} , cause: {}", job.getId(), e.getMessage());
            } finally {
                jobRepository.save(job);
            }
        });
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
