package com.agile.daemon.quartz.event;

import com.agile.daemon.quartz.entity.SysJobLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Scheduled job log event.
 *
 * @author Huang Z.Y.
 */
@Getter
@AllArgsConstructor
public class SysJobLogEvent {

    private final SysJobLog sysJobLog;

}
