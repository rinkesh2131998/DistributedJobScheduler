package com.personal.job_scheduler.service.jobs;

import com.personal.job_scheduler.exception.JobHandlerException;
import com.personal.job_scheduler.models.entity.enums.JobActionType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JobHandlerRegistry {
    private final Map<JobActionType, JobHandler> jobHandlerMap = new HashMap<>();

    public void registerHandler(final JobActionType jobActionType, final JobHandler jobHandler) {
        if (jobActionType == null || jobHandler == null) {
            log.warn("Invalid jobAction or handler, unable to set job handler, action: {}, handler: {} ", jobActionType, jobHandler);
            throw new JobHandlerException("Null value for action/handler");
        }
        jobHandlerMap.put(jobActionType, jobHandler);
    }

    public JobHandler getJobHandler(final JobActionType jobActionType) {
        if (jobActionType == null) {
            log.warn("Null jobAction, unable to get job handler");
            throw new JobHandlerException("Null value for action");
        }
        return jobHandlerMap.get(jobActionType);
    }

    public List<JobActionType> getAllHandlers() {
        return jobHandlerMap.keySet().stream().toList();
    }
}
