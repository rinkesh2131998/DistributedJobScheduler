package com.personal.job_scheduler.config;

import com.personal.job_scheduler.models.entity.enums.JobActionType;
import com.personal.job_scheduler.service.jobs.JobHandlerRegistry;
import com.personal.job_scheduler.service.jobs.handlers.EchoJobHandler;
import com.personal.job_scheduler.service.jobs.handlers.HttpJobHandler;
import com.personal.job_scheduler.service.jobs.handlers.SleepJobHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobHandlerConfig {

    @Bean
    public JobHandlerRegistry jobHandlerRegistry() {
        final JobHandlerRegistry jobHandlerRegistry = new JobHandlerRegistry();
        jobHandlerRegistry.registerHandler(JobActionType.HTTP, new HttpJobHandler());
        jobHandlerRegistry.registerHandler(JobActionType.ECHO, new EchoJobHandler());
        jobHandlerRegistry.registerHandler(JobActionType.SLEEP, new SleepJobHandler());
        return jobHandlerRegistry;
    }
}
