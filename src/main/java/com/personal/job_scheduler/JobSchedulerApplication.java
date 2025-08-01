package com.personal.job_scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JobSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobSchedulerApplication.class, args);
    }

}
