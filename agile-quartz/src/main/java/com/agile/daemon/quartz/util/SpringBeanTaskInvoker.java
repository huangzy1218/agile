package com.agile.daemon.quartz.util;

import cn.hutool.core.util.StrUtil;
import com.agile.common.core.util.SpringContextHolder;
import com.agile.daemon.quartz.constant.AgileQuartzEnum;
import com.agile.daemon.quartz.entity.SysJob;
import com.agile.daemon.quartz.exception.TaskException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Timing task spring bean reflection implementation.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@Component
public class SpringBeanTaskInvoker implements ITaskInvoker {

    @Override
    public void invoke(SysJob sysJob) throws TaskException {
        Object target;
        Method method;
        Object returnValue;
        // If you look for it through the Spring context, may not be able to find it.
        target = SpringContextHolder.getBean(sysJob.getClassName());
        try {
            if (StrUtil.isNotEmpty(sysJob.getMethodParamsValue())) {
                method = target.getClass().getDeclaredMethod(sysJob.getMethodName(), String.class);
                ReflectionUtils.makeAccessible(method);
                returnValue = method.invoke(target, sysJob.getMethodParamsValue());
            } else {
                method = target.getClass().getDeclaredMethod(sysJob.getMethodName());
                ReflectionUtils.makeAccessible(method);
                returnValue = method.invoke(target);
            }
            if (StrUtil.isEmpty(returnValue.toString())
                    || AgileQuartzEnum.JOB_LOG_STATUS_FAIL.getType().equals(returnValue.toString())) {
                log.error("Scheduled task SpringBeanTaskInvoker exception, execution task：{}", sysJob.getClassName());
                throw new TaskException("The scheduled task springBeanTaskInvoker business execution failed, task: " +
                        sysJob.getClassName());
            }
        } catch (NoSuchMethodException e) {
            log.error("Scheduled task spring bean reflection exception method not found, execution task: {}", sysJob.getClassName());
            throw new TaskException("The scheduled task spring bean reflection exception method was not found. Execute the task: " +
                    sysJob.getClassName());
        } catch (IllegalAccessException e) {
            log.error("Scheduled task spring bean reflection exception, execution task: {}", sysJob.getClassName());
            throw new TaskException("Scheduled task spring bean reflection exception, execution task: " + sysJob.getClassName());
        } catch (InvocationTargetException e) {
            log.error("Scheduled task spring bean reflection execution exception, execution task: {}", sysJob.getClassName());
            throw new TaskException("Scheduled task spring bean reflection execution exception, execution task: " + sysJob.getClassName());
        }
    }

}
