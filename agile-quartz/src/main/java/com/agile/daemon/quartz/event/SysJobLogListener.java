package com.agile.daemon.quartz.event;

import com.agile.daemon.quartz.entity.SysJobLog;
import com.agile.daemon.quartz.service.SysJobLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Huang Z.Y.
 */
@Slf4j
@AllArgsConstructor
public class SysJobLogListener {

    private final SysJobLogService sysJobLogService;

    @Async
    @Order
    @EventListener(SysJobLogEvent.class)
    public void saveSysJobLog(SysJobLogEvent event) {
        SysJobLog sysJobLog = event.getSysJobLog();
        sysJobLogService.save(sysJobLog);
        log.info("Execute scheduled task log, task name: {}", sysJobLog.getJobName());
    }

}
