package com.agile.daemon.quartz.config;

import com.agile.daemon.quartz.constant.AgileQuartzEnum;
import com.agile.daemon.quartz.entity.SysJob;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@AllArgsConstructor
@DisallowConcurrentExecution
public class AgileQuartzFactory implements Job {

    private AgileQuartzInvokerFactory agileQuartzInvokerFactory;

    @Override
    @SneakyThrows
    public void execute(JobExecutionContext jobExecutionContext) {
        SysJob sysJob = (SysJob) jobExecutionContext.getMergedJobDataMap()
                .get(AgileQuartzEnum.SCHEDULE_JOB_KEY.getType());
        agileQuartzInvokerFactory.init(sysJob, jobExecutionContext.getTrigger());
    }

}

