package com.personal.job_scheduler.repository;

import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.JobStatus;
import com.personal.job_scheduler.models.entity.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {

    List<Job> findByJobStatus(JobStatus jobStatus);

    List<Job> findByType(JobType jobType);

}
