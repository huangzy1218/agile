package com.agile.common.log.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.agile.common.core.util.SpringContextHolder;
import com.agile.common.log.config.AgileLogProperties;
import com.agile.common.log.event.LogEventSource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * 日志工具类。
 *
 * @author Huang Z.Y.
 */
@UtilityClass
public class LogUtils {

    public LogEventSource getSysLog() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        LogEventSource log = new LogEventSource();
        log.setLogType(LogTypeEnum.NORMAL.getType());
        log.setRequestUri(URLUtil.getPath(request.getRequestURI()));
        log.setMethod(request.getMethod());
        log.setRemoteAddr(JakartaServletUtil.getClientIP(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setCreateBy(getUsername());
        log.setServiceId(SpringUtil.getProperty("spring.application.name"));

        // get 参数脱敏
        AgileLogProperties logProperties = SpringContextHolder.getBean(AgileLogProperties.class);
        Map<String, String[]> paramsMap = MapUtil.removeAny(request.getParameterMap(),
                ArrayUtil.toArray(logProperties.getExcludeFields(), String.class));
        log.setParams(HttpUtil.toParams(paramsMap));
        return log;
    }

    /**
     * 获取用户名称
     *
     * @return username
     */
    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

    /**
     * 获取spel 定义的参数值
     *
     * @param context 参数容器
     * @param key     key
     * @param clazz   需要返回的类型
     * @param <T>     返回泛型
     * @return 参数值
     */
    public <T> T getValue(EvaluationContext context, String key, Class<T> clazz) {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(key);
        return expression.getValue(context, clazz);
    }

    /**
     * 获取参数容器
     *
     * @param arguments       方法的参数列表
     * @param signatureMethod 被执行的方法体
     * @return 装载参数的容器
     */
    public EvaluationContext getContext(Object[] arguments, Method signatureMethod) {
        String[] parameterNames = new StandardReflectionParameterNameDiscoverer().getParameterNames(signatureMethod);
        EvaluationContext context = new StandardEvaluationContext();
        if (parameterNames == null) {
            return context;
        }
        for (int i = 0; i < arguments.length; i++) {
            context.setVariable(parameterNames[i], arguments[i]);
        }
        return context;
    }
}
    