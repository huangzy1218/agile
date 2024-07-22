package com.agile.daemon.quartz.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.agile.daemon.quartz.entity.SysJob;
import com.agile.daemon.quartz.exception.TaskException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Rest reflection implementation of scheduled tasks.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Component
public class RestTaskInvoker implements ITaskInvoker {

    @Override
    public void invoke(SysJob sysJob) throws TaskException {
        try {
            HttpRequest request = HttpUtil.createGet(sysJob.getExecutePath());
            request.execute();
        } catch (Exception e) {
            log.error("Timing task restTaskInvoker exception, execution task: {}", sysJob.getExecutePath());
            throw new TaskException("The scheduled task restTaskInvoker business execution failed, and the task：" + sysJob.getExecutePath());
        }
    }

}
