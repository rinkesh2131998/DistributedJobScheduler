package com.personal.job_scheduler.service.executor;

import com.personal.job_scheduler.exception.JobHandlerException;
import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.JobRunHistory;
import com.personal.job_scheduler.repository.JobHistoryRepository;
import com.personal.job_scheduler.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobPersistenceService {
    private static final Logger log = LoggerFactory.getLogger(JobPersistenceService.class);
    private final JobRepository jobRepository;
    private final JobHistoryRepository jobHistoryRepository;

    @Transactional
    public void saveJobAndRunHistoryOnTransaction(final Job job, final JobRunHistory jobRunHistory) {
        try {
            jobHistoryRepository.save(jobRunHistory);
            jobRepository.save(job);
        } catch (Exception e) {
            log.error("Error Saving jobs and run history to db");
            throw new JobHandlerException(e.getMessage());
        }
    }
}
