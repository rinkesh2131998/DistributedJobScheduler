package com.personal.job_scheduler.models.entity;

import com.personal.job_scheduler.models.entity.enums.JobStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_run_history", schema = "scheduler")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRunHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    private int attemptNumber;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private LocalDateTime pickedAt;
    private JobStatus status;
    @Lob
    private String result;
    @Lob
    private String errorMessage;
}
