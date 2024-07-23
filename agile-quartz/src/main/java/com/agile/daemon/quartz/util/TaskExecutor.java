package com.agile.daemon.quartz.util;

import com.agile.daemon.quartz.config.AgileQuartzFactory;
import com.agile.daemon.quartz.constant.AgileQuartzEnum;
import com.agile.daemon.quartz.entity.SysJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@Component
public class TaskExecutor {

    /**
     * Get the unique key of the scheduled task.
     *
     * @param sysjob Scheduled job
     * @return Job key
     */
    public static JobKey getJobKey(SysJob sysjob) {
        return JobKey.jobKey(sysjob.getJobName(), sysjob.getJobGroup());
    }

    /**
     * Get the unique key of the scheduled task trigger cron.
     *
     * @param sysjob Scheduled job
     * @return Trigger key
     */
    public static TriggerKey getTriggerKey(SysJob sysjob) {
        return TriggerKey.triggerKey(sysjob.getJobName(), sysjob.getJobGroup());
    }

    /**
     * Execute a task immediately.
     */
    public static boolean runOnce(Scheduler scheduler, SysJob sysJob) {
        try {
            // Parameter
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(AgileQuartzEnum.SCHEDULE_JOB_KEY.getType(), sysJob);

            scheduler.triggerJob(getJobKey(sysJob), dataMap);
        } catch (SchedulerException e) {
            log.error("Execute scheduled tasks immediately, failure information: {}", e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Add or update scheduled task.
     *
     * @param sysjob
     * @param scheduler
     */
    public void addOrUpdateJob(SysJob sysjob, Scheduler scheduler) {
        CronTrigger trigger = null;
        try {
            JobKey jobKey = getJobKey(sysjob);
            // Get trigger
            TriggerKey triggerKey = getTriggerKey(sysjob);
            trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // Determine whether the trigger exists
            // (if it exists, it means it has been run before but is currently disabled, if it does not exist, it has never been run once)
            if (trigger == null) {
                // Create a new work task and specify the task type to be performed in series
                JobDetail jobDetail = JobBuilder.newJob(AgileQuartzFactory.class).withIdentity(jobKey).build();
                // Add task information to task information
                jobDetail.getJobDataMap().put(AgileQuartzEnum.SCHEDULE_JOB_KEY.getType(), sysjob);
                // Convert cron expression
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(sysjob.getCronExpression());
                cronScheduleBuilder = this.handleCronScheduleMisfirePolicy(sysjob, cronScheduleBuilder);
                // Create a trigger and plug in the cron expression object
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey)
                        .withSchedule(cronScheduleBuilder)
                        .build();
                // Combine triggers and tasks in the scheduler
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(sysjob.getCronExpression());
                cronScheduleBuilder = this.handleCronScheduleMisfirePolicy(sysjob, cronScheduleBuilder);
                // Follow the new rules
                trigger = trigger.getTriggerBuilder()
                        .withIdentity(triggerKey)
                        .withSchedule(cronScheduleBuilder)
                        .build();
                // Update task information into task information
                trigger.getJobDataMap().put(AgileQuartzEnum.SCHEDULE_JOB_KEY.getType(), sysjob);
                // Restart
                scheduler.rescheduleJob(triggerKey, trigger);
            }
            // If the task status is suspended
            if (sysjob.getJobStatus().equals(AgileQuartzEnum.JOB_STATUS_NOT_RUNNING.getType())) {
                this.pauseJob(sysjob, scheduler);
            }
        } catch (SchedulerException e) {
            log.error("Add or update scheduled tasks, failure information: {}", e.getMessage());
        }
    }

    /**
     * Pause scheduled task.
     *
     * @param sysjob    Scheduled job
     * @param scheduler Scheduler
     */
    public void pauseJob(SysJob sysjob, Scheduler scheduler) {
        try {
            if (scheduler != null) {
                scheduler.pauseJob(getJobKey(sysjob));
            }
        } catch (SchedulerException e) {
            log.error("Failed to suspend task, failure message: {}", e.getMessage());
        }

    }

    /**
     * Resume scheduled task.
     *
     * @param sysjob    Scheduled job
     * @param scheduler Scheduler
     */
    public void resumeJob(SysJob sysjob, Scheduler scheduler) {
        try {
            if (scheduler != null) {
                scheduler.resumeJob(getJobKey(sysjob));
            }
        } catch (SchedulerException e) {
            log.error("The recovery task failed, failure information: {}", e.getMessage());
        }

    }

    /**
     * Remove scheduled task.
     *
     * @param sysjob    Scheduled job
     * @param scheduler Scheduler
     */
    public void removeJob(SysJob sysjob, Scheduler scheduler) {
        try {
            if (scheduler != null) {
                // Stop trigger
                scheduler.pauseTrigger(getTriggerKey(sysjob));
                // Remove trigger
                scheduler.unscheduleJob(getTriggerKey(sysjob));
                // Delete job
                scheduler.deleteJob(getJobKey(sysjob));
            }
        } catch (Exception e) {
            log.error("Failed to remove the scheduled task, failure message: {}", e.getMessage());
        }
    }

    /**
     * Start all scheduled task.
     *
     * @param scheduler Scheduler
     */
    public void startJobs(Scheduler scheduler) {
        try {
            if (scheduler != null) {
                scheduler.resumeAll();
            }
        } catch (SchedulerException e) {
            log.error("Failed to start all running scheduled tasks, failure message: {}", e.getMessage());
        }
    }

    /**
     * Stop all running scheduled task.
     *
     * @param scheduler Scheduler
     */
    public void pauseJobs(Scheduler scheduler) {
        try {
            if (scheduler != null) {
                scheduler.pauseAll();
            }
        } catch (Exception e) {
            log.error("Failed to suspend all running scheduled tasks, failure message: {}", e.getMessage());
        }
    }

    /**
     * Get the missed execution strategy method.
     *
     * @param sysJob
     * @param cronScheduleBuilder
     * @return
     */
    private CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJob sysJob,
                                                                CronScheduleBuilder cronScheduleBuilder) {
        if (AgileQuartzEnum.MISFIRE_DEFAULT.getType().equals(sysJob.getMisfirePolicy())) {
            return cronScheduleBuilder;
        } else if (AgileQuartzEnum.MISFIRE_IGNORE_MISFIRES.getType().equals(sysJob.getMisfirePolicy())) {
            return cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
        } else if (AgileQuartzEnum.MISFIRE_FIRE_AND_PROCEED.getType().equals(sysJob.getMisfirePolicy())) {
            return cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
        } else if (AgileQuartzEnum.MISFIRE_DO_NOTHING.getType().equals(sysJob.getMisfirePolicy())) {
            return cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
        } else {
            return cronScheduleBuilder;
        }
    }

    /**
     * Determine whether the cron expression is correct.
     *
     * @param cronExpression Cron expression
     * @return {@code true} for success
     */
    public boolean isValidCron(String cronExpression) {
        return CronExpression.isValidExpression(cronExpression);
    }

}
