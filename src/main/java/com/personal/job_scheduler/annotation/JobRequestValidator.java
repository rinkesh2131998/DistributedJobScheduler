package com.personal.job_scheduler.annotation;

import com.personal.job_scheduler.models.dto.JobCreateRequest;
import com.personal.job_scheduler.models.dto.JobUpdateRequest;
import com.personal.job_scheduler.models.entity.JobType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class JobRequestValidator implements ConstraintValidator<ValidJobRequests, Object> {
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o instanceof JobCreateRequest) {
            return validateJobTypeRequiredArgs(((JobCreateRequest) o).jobType(), ((JobCreateRequest) o).cronExpression(), ((JobCreateRequest) o).scheduledTime());
        } else if (o instanceof JobUpdateRequest) {
            return validateJobTypeRequiredArgs(((JobUpdateRequest) o).jobType(), ((JobUpdateRequest) o).cronExpression(), ((JobUpdateRequest) o).scheduledTime());
        }
        return false;
    }

    private boolean validateJobTypeRequiredArgs(final JobType jobType, final String cronExpression, final LocalDateTime scheduledTime) {
        switch (jobType) {
            case CRON -> {
                return cronExpression != null && scheduledTime == null;
            }
            case ONE_TIME -> {
                return scheduledTime != null && cronExpression == null;
            }
            case MANUAL -> {
                return cronExpression == null && scheduledTime == null;
            }
        }
        return false;
    }
}
