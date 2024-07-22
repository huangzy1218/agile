package com.agile.daemon.quartz.util;

import cn.hutool.core.util.StrUtil;
import com.agile.daemon.quartz.constant.AgileQuartzEnum;
import com.agile.daemon.quartz.entity.SysJob;
import com.agile.daemon.quartz.entity.SysJobLog;
import com.agile.daemon.quartz.service.SysJobService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

/**
 * Task invoker utility.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskInvokerUtils {

    private final ApplicationEventPublisher publisher;

    private final SysJobService sysJobService;

    @SneakyThrows
    public void invoke(SysJob sysJob, Trigger trigger) {
        // Execution start time
        long startTime;
        // Execution end time
        long endTime;
        // Get execution start time
        startTime = System.currentTimeMillis();
        // Update the status, execution time, last execution time,
        // next execution time and other information in the scheduled task table
        SysJob updateSysjob = new SysJob();
        updateSysjob.setJobId(sysJob.getJobId());
        // Log
        SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobId(sysJob.getJobId());
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setJobGroup(sysJob.getJobGroup());
        sysJobLog.setJobOrder(sysJob.getJobOrder());
        sysJobLog.setJobType(sysJob.getJobType());
        sysJobLog.setExecutePath(sysJob.getExecutePath());
        sysJobLog.setClassName(sysJob.getClassName());
        sysJobLog.setMethodName(sysJob.getMethodName());
        sysJobLog.setMethodParamsValue(sysJob.getMethodParamsValue());
        sysJobLog.setCronExpression(sysJob.getCronExpression());
        try {
            // Perform task
            ITaskInvoker iTaskInvoker = TaskInvokerFactory.getInvoker(sysJob.getJobType());
            // Ensure that the tenant context has a value so that the multi-tenant feature
            // in the current thread takes effect.
            iTaskInvoker.invoke(sysJob);
            // Record success status
            sysJobLog.setJobMessage(AgileQuartzEnum.JOB_LOG_STATUS_SUCCESS.getDescription());
            sysJobLog.setJobLogStatus(AgileQuartzEnum.JOB_LOG_STATUS_SUCCESS.getType());
            // Task list information update
            updateSysjob.setJobExecuteStatus(AgileQuartzEnum.JOB_LOG_STATUS_SUCCESS.getType());
        } catch (Throwable e) {
            log.error("定时任务执行失败，任务名称：{}；任务组名：{}，cron执行表达式：{}，执行时间：{}", sysJob.getJobName(), sysJob.getJobGroup(),
                    sysJob.getCronExpression(), new Date());
            // Log failure status
            sysJobLog.setJobMessage(AgileQuartzEnum.JOB_LOG_STATUS_FAIL.getDescription());
            sysJobLog.setJobLogStatus(AgileQuartzEnum.JOB_LOG_STATUS_FAIL.getType());
            sysJobLog.setExceptionInfo(StrUtil.sub(e.getMessage(), 0, 2000));
            // Task list information update
            updateSysjob.setJobExecuteStatus(AgileQuartzEnum.JOB_LOG_STATUS_FAIL.getType());
        } finally {
            // Record the execution time and execute it immediately using simple Teigger
            if (trigger instanceof CronTrigger) {
                updateSysjob
                        .setStartTime(trigger.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                updateSysjob.setPreviousTime(
                        trigger.getPreviousFireTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                updateSysjob.setNextTime(
                        trigger.getNextFireTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            }
            // Record execution time
            endTime = System.currentTimeMillis();
            sysJobLog.setExecuteTime(String.valueOf(endTime - startTime));

            publisher.publishEvent(new SysJobLogEvent(sysJobLog));
            sysJobService.updateById(updateSysjob);
        }
    }

}
