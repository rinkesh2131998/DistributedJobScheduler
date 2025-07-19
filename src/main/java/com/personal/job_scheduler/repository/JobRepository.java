package com.personal.job_scheduler.repository;

import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {

    List<Job> findByType(JobType jobType);

    @Query(value = """
            SELECT * FROM scheduler.job
                        WHERE job_status = 'SCHEDULED'
                          AND (
                            (type = 'ONE_TIME' AND scheduled_time <= now())
                            OR (type = 'CRON' AND cron_expression IS NOT NULL)
                          )
                          AND (picked_at IS NULL OR picked_at < now() - interval '1 minute')
                        ORDER BY created_at
                        LIMIT :limit
                        FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<Job> findAndLockEligibleJobs(@Param("limit") int limit);

}
