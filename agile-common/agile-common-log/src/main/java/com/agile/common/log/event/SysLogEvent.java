package com.agile.common.log.event;

import com.agile.admin.api.entity.SysLog;
import org.springframework.context.ApplicationEvent;

/**
 * System log event.
 *
 * @author Huang Z.Y.
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(SysLog source) {
        super(source);
    }

}
    