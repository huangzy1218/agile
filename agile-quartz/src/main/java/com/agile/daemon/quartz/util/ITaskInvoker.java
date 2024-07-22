package com.agile.daemon.quartz.util;

import com.agile.daemon.quartz.entity.SysJob;
import com.agile.daemon.quartz.exception.TaskException;

/**
 * Scheduled task reflection implementation interface class.
 *
 * @author Huang Z.Y.
 */
public interface ITaskInvoker {

    /**
     * Execute reflection method.
     *
     * @param sysJob Task configuration
     * @throws TaskException When invoke occurs exception.
     */
    void invoke(SysJob sysJob) throws TaskException;

}
