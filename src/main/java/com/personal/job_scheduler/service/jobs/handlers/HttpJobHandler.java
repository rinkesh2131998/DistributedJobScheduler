package com.personal.job_scheduler.service.jobs.handlers;

import com.personal.job_scheduler.models.entity.Job;
import com.personal.job_scheduler.models.entity.JobRunHistory;
import com.personal.job_scheduler.models.entity.enums.JobActionType;
import com.personal.job_scheduler.service.jobs.JobHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class HttpJobHandler implements JobHandler {
    @Override
    public boolean canHandle(Job job) {
        return job.getJobActionType() == JobActionType.HTTP;
    }

    @Override
    public void execute(Job job, JobRunHistory jobRunHistory) {
        try {
            if ("FAIL_THIS_JOB".equals(job.getPayload())) {
                throw new IllegalStateException("Forced failure for retry test");
            }
            URL url = new URL(job.getPayload());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line);
            }

            in.close();
            con.disconnect();

            jobRunHistory.setResult("HTTP " + status + ": " + content);
        } catch (Exception e) {
            throw new RuntimeException("HTTP job failed: " + e.getMessage(), e);
        }
    }
}
