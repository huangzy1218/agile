package com.agile.common.log.event;

import com.agile.common.log.entity.Log;
import org.springframework.context.ApplicationEvent;

/**
 * 日志事件。
 *
 * @author Huang Z.Y.
 */
public class LogEvent extends ApplicationEvent {

    public LogEvent(Log source) {
        super(source);
    }
}
    