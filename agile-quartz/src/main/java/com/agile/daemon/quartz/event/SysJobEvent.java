package com.agile.daemon.quartz.event;

import com.agile.daemon.quartz.entity.SysJob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.quartz.Trigger;

/**
 * Scheduled task multi-threaded event.
 *
 * @author Huang Z.Y.
 */
@Getter
@AllArgsConstructor
public class SysJobEvent {

    private final SysJob sysJob;

    private final Trigger trigger;

}
