package com.agile.daemon.quartz.util;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.agile.daemon.quartz.constant.AgileQuartzEnum;
import com.agile.daemon.quartz.entity.SysJob;
import com.agile.daemon.quartz.exception.TaskException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * Timing task java class reflection implementation.
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class JavaClassTaskInvoker implements ITaskInvoker {

    @Override
    public void invoke(SysJob sysJob) throws TaskException {
        Object obj;
        Class clazz;
        Method method;
        Object returnValue;
        try {
            if (CharSequenceUtil.isNotEmpty(sysJob.getMethodParamsValue())) {
                clazz = Class.forName(sysJob.getClassName());
                obj = clazz.newInstance();
                method = clazz.getDeclaredMethod(sysJob.getMethodName(), String.class);
                returnValue = method.invoke(obj, sysJob.getMethodParamsValue());
            } else {
                clazz = Class.forName(sysJob.getClassName());
                obj = clazz.newInstance();
                method = clazz.getDeclaredMethod(sysJob.getMethodName());
                returnValue = method.invoke(obj);
            }
            if (StrUtil.isEmpty(returnValue.toString())
                    || AgileQuartzEnum.JOB_LOG_STATUS_FAIL.getType().equals(returnValue.toString())) {
                log.error("Scheduled task JavaClassTaskInvoker exception, execution task:{}", sysJob.getClassName());
                throw new TaskException("Scheduled task JavaClassTaskInvoker exception, execution task: " + sysJob.getClassName());
            }
        } catch (ClassNotFoundException e) {
            log.error("The scheduled task java reflection class is not found and the task is executed：{}", sysJob.getClassName());
            throw new TaskException("The scheduled task java reflection class is not found and the task is executed: " + sysJob.getClassName());
        } catch (Exception e) {
            log.error("Timing task java reflection class exception, execution task: {}", sysJob.getClassName());
            throw new TaskException("Timing task java reflection class exception, execution task: " + sysJob.getClassName());
        }
    }

}
