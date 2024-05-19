package com.agile.common.log.aspect;

import cn.hutool.core.util.StrUtil;
import com.agile.common.core.util.SpringContextHolder;
import com.agile.common.log.annotation.SysLog;
import com.agile.common.log.event.SysLogEvent;
import com.agile.common.log.event.SysLogEventSource;
import com.agile.common.log.util.LogTypeEnum;
import com.agile.common.log.util.SysLogUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;

/**
 * Operation logs are stored asynchronously using Spring Event.
 *
 * @author Huang Z.Y.
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class SysLogAspect {

    @Around("@annotation(sysLog)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint point, SysLog sysLog) {
        String strClassName = point.getTarget().getClass().getName();
        String strMethodName = point.getSignature().getName();
        log.debug("[ClassName]:{},[Method]:{}", strClassName, strMethodName);

        String value = sysLog.value();
        String expression = sysLog.expression();
        // The current expression has SpEL, which overrides the value of value
        if (StrUtil.isNotBlank(expression)) {
            // Parse SpEL
            MethodSignature signature = (MethodSignature) point.getSignature();
            EvaluationContext context = SysLogUtils.getContext(point.getArgs(), signature.getMethod());
            try {
                value = SysLogUtils.getValue(context, expression, String.class);
            } catch (Exception e) {
                // SpEL expression exception, get the value of value
                log.error("@AgileLog resolve the SpEL {} exception", expression);
            }
        }

        SysLogEventSource logVo = SysLogUtils.getSysLog();
        logVo.setTitle(value);
        // Gets the request body parameter
        if (StrUtil.isBlank(logVo.getParams())) {
            logVo.setBody(point.getArgs());
        }
        // Send asynchronous log events
        Long startTime = System.currentTimeMillis();
        Object obj;

        try {
            obj = point.proceed();
        } catch (Exception e) {
            logVo.setLogType(LogTypeEnum.ERROR.getType());
            logVo.setException(e.getMessage());
            throw e;
        } finally {
            Long endTime = System.currentTimeMillis();
            logVo.setTime(endTime - startTime);
            SpringContextHolder.publishEvent(new SysLogEvent(logVo));
        }

        return obj;
    }
    
}
    