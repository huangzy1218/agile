package com.agile.common.log.aspect;

import cn.hutool.core.util.StrUtil;
import com.agile.common.core.util.SpringContextHolder;
import com.agile.common.log.annotation.AgileLog;
import com.agile.common.log.event.LogEvent;
import com.agile.common.log.event.LogEventSource;
import com.agile.common.log.util.LogTypeEnum;
import com.agile.common.log.util.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;

/**
 * 操作日志使用SpringEvent异步入库。
 *
 * @author Huang Z.Y.
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LogAspect {

    @Around("@annotation(sysLog)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint point, AgileLog sysLog) {
        String strClassName = point.getTarget().getClass().getName();
        String strMethodName = point.getSignature().getName();
        log.debug("[ClassName]:{},[Method]:{}", strClassName, strMethodName);

        String value = sysLog.value();
        String expression = sysLog.expression();
        // 当前表达式存在 SPEL，会覆盖 value 的值
        if (StrUtil.isNotBlank(expression)) {
            // 解析SPEL
            MethodSignature signature = (MethodSignature) point.getSignature();
            EvaluationContext context = LogUtils.getContext(point.getArgs(), signature.getMethod());
            try {
                value = LogUtils.getValue(context, expression, String.class);
            } catch (Exception e) {
                // SPEL 表达式异常，获取 value 的值
                log.error("@AgileLog 解析SPEL {} 异常", expression);
            }
        }

        LogEventSource logVo = LogUtils.getSysLog();
        logVo.setTitle(value);
        // 获取请求body参数
        if (StrUtil.isBlank(logVo.getParams())) {
            logVo.setBody(point.getArgs());
        }
        // 发送异步日志事件
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
            SpringContextHolder.publishEvent(new LogEvent(logVo));
        }

        return obj;
    }
}
    