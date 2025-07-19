package com.personal.job_scheduler.util;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class CronUtils {

    public static boolean isDue(final String cron, final LocalDateTime dateToCompareAgainst) {
        if (cron == null || cron.isBlank()) {
            return false;
        }
        try {
            final CronExpression cronExpression = new CronExpression(cron);
            final Date nowDate = Date.from(dateToCompareAgainst.atZone(ZoneId.systemDefault()).toInstant());
            final Date nextValidTime = cronExpression.getNextValidTimeAfter(Date
                    .from(dateToCompareAgainst.minusSeconds(1)
                            .atZone(ZoneId.systemDefault()).toInstant()));
            return nextValidTime != null && Math.abs(nextValidTime.getTime() - nowDate.getTime()) < 1000;
        } catch (ParseException e) {
            log.warn("Incorrect cron expression found : {}", cron);
            return false;
        } catch (Exception exception) {
            return false;
        }
    }

}
