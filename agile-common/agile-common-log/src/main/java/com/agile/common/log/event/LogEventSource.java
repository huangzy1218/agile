package com.agile.common.log.event;

import com.agile.common.log.entity.Log;
import lombok.Data;

/**
 * SpringEvent事件日志。
 *
 * @author Huang Z.Y.
 */
@Data
public class LogEventSource extends Log {

    /**
     * 参数重写为Object，存储事件相关数据
     */
    private Object body;
}
    