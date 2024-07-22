package com.agile.daemon.quartz.util;

import cn.hutool.core.util.StrUtil;
import com.agile.daemon.quartz.entity.SysJob;
import com.agile.daemon.quartz.exception.TaskException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Timing task java jar reflection implementation.
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class JarTaskInvoker implements ITaskInvoker {

    @Override
    public void invoke(SysJob sysJob) throws TaskException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        File jar = new File(sysJob.getExecutePath());
        processBuilder.directory(jar.getParentFile());
        List<String> commands = new ArrayList<>();
        commands.add("java");
        commands.add("-jar");
        commands.add(sysJob.getExecutePath());
        if (StrUtil.isNotEmpty(sysJob.getMethodParamsValue())) {
            commands.add(sysJob.getMethodParamsValue());
        }
        processBuilder.command(commands);
        try {
            processBuilder.start();
        } catch (IOException e) {
            log.error("Scheduled task jar reflection execution exception, execution task: {}", sysJob.getExecutePath());
            throw new TaskException("Scheduled task jar reflection execution exception, execution task: " + sysJob.getExecutePath());
        }
    }

}
