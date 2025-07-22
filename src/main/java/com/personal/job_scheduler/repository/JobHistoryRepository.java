package com.personal.job_scheduler.repository;

import com.personal.job_scheduler.models.entity.JobRunHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobHistoryRepository extends JpaRepository<JobRunHistory, UUID> {

    Page<JobRunHistory> findByJobId(UUID jobId, Pageable pageable);

    int countByJobId(UUID jobId);

}
