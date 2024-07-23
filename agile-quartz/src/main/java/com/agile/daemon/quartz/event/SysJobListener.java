package com.agile.daemon.quartz.event;

import com.agile.daemon.quartz.entity.SysJob;
import com.agile.daemon.quartz.util.TaskInvokerUtils;
import lombok.AllArgsConstructor;
import org.quartz.Trigger;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * Asynchronously monitor scheduled task event.
 *
 * @author Huang Z.Y.
 */
@AllArgsConstructor
public class SysJobListener {

    private final TaskInvokerUtils utils;

    @Async
    @Order
    @EventListener(SysJobEvent.class)
    public void consumeSysJob(SysJobEvent event) {
        SysJob sysJob = event.getSysJob();
        Trigger trigger = event.getTrigger();
        utils.invoke(sysJob, trigger);
    }

}
