package com.agile.daemon.quartz.util;

import cn.hutool.core.util.StrUtil;
import com.agile.common.core.util.SpringContextHolder;
import com.agile.daemon.quartz.constant.JobTypeQuartzEnum;
import com.agile.daemon.quartz.exception.TaskException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Huang Z.Y.
 */
@Slf4j
public class TaskInvokerFactory {

    public static ITaskInvoker getInvoker(String jobType) throws TaskException {
        if (StrUtil.isBlank(jobType)) {
            log.info("There is an error in getting the parameter passed by TaskInvoker, jobType: {}", jobType);
            throw new TaskException("");
        }

        ITaskInvoker iTaskInvoker = null;
        if (JobTypeQuartzEnum.JAVA.getType().equals(jobType)) {
            iTaskInvoker = SpringContextHolder.getBean("javaClassTaskInvoker");
        } else if (JobTypeQuartzEnum.SPRING_BEAN.getType().equals(jobType)) {
            iTaskInvoker = SpringContextHolder.getBean("springBeanTaskInvoker");
        } else if (JobTypeQuartzEnum.REST.getType().equals(jobType)) {
            iTaskInvoker = SpringContextHolder.getBean("restTaskInvoker");
        } else if (JobTypeQuartzEnum.JAR.getType().equals(jobType)) {
            iTaskInvoker = SpringContextHolder.getBean("jarTaskInvoker");
        } else if (StrUtil.isBlank(jobType)) {
            log.info("There is no corresponding reflection method for the scheduled task type. Reflection type: {}", jobType);
            throw new TaskException("");
        }

        return iTaskInvoker;
    }

}
