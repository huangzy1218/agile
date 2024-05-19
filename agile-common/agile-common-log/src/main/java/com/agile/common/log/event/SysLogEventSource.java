package com.agile.common.log.event;

import com.agile.admin.api.entity.SysLog;
import lombok.Data;

/**
 * SpringEvent事件日志。
 *
 * @author Huang Z.Y.
 */
@Data
public class SysLogEventSource extends SysLog {

    /**
     * The parameter is rewritten to Object to store event-related data.
     */
    private Object body;

}
    