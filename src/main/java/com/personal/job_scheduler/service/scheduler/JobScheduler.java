package com.personal.job_scheduler.service.scheduler;

import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.enums.JobStatus;
import com.personal.job_scheduler.repository.JobRepository;
import com.personal.job_scheduler.service.executor.JobExecutor;
import com.personal.job_scheduler.util.CronUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(name = "scheduler.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class JobScheduler {
    private static final int BATCH_SIZE = 50;

    private final JobRepository jobRepository;
    private final JobExecutor jobExecutor;

    @Scheduled(fixedDelayString = "${job.scheduler.fixed-delay:5000}")
    @Transactional
    public void pollAndPickScheduledJobs() {
        log.debug("Scheduling jobs...");
        log.warn("Testing if it is proccessing or not");

        // Fetch all scheduled jobs
        final List<Job> jobsToCheck = jobRepository.findAndLockEligibleJobs(BATCH_SIZE);
        for (var job : jobsToCheck) {
            if (shouldSubmitJob(job)) {
                job.setPickedAt(LocalDateTime.now());
                job.setJobStatus(JobStatus.RUNNING);
                jobRepository.save(job);
                jobExecutor.submit(job);
            }
        }
        log.debug("Scheduler finished {} job(s).", jobsToCheck.size());
    }

    private boolean shouldSubmitJob(final Job job) {
        switch (job.getType()) {
            case CRON -> {
                return CronUtils.isDue(job.getCronExpression(), LocalDateTime.now());
            }
            case ONE_TIME -> {
                return job.getScheduledTime() != null && job.getScheduledTime().isBefore(LocalDateTime.now());
            }
            case MANUAL -> {
                log.debug("Skipping Manual job: {}", job.getId());
                return false;
            }
        }
        return false;
    }
}
