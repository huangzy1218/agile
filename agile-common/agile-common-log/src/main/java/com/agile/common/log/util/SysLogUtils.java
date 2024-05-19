package com.agile.common.log.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.agile.common.core.util.SpringContextHolder;
import com.agile.common.log.config.AgileLogProperties;
import com.agile.common.log.event.SysLogEventSource;
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
public class SysLogUtils {

    public SysLogEventSource getSysLog() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        SysLogEventSource log = new SysLogEventSource();
        log.setLogType(LogTypeEnum.NORMAL.getType());
        log.setRequestUri(URLUtil.getPath(request.getRequestURI()));
        log.setMethod(request.getMethod());
        log.setRemoteAddr(JakartaServletUtil.getClientIP(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setCreateBy(getUsername());
        log.setServiceId(SpringUtil.getProperty("spring.application.name"));

        // GET parametric desensitization
        AgileLogProperties logProperties = SpringContextHolder.getBean(AgileLogProperties.class);
        Map<String, String[]> paramsMap = MapUtil.removeAny(request.getParameterMap(),
                ArrayUtil.toArray(logProperties.getExcludeFields(), String.class));
        log.setParams(HttpUtil.toParams(paramsMap));
        return log;
    }

    /**
     * Get username.
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
     * Get the parameter values defined by SpEL.
     *
     * @param context parameter context
     * @param key     key
     * @param clazz   the type to be returned
     * @param <T>     return generic
     * @return parameter value
     */
    public <T> T getValue(EvaluationContext context, String key, Class<T> clazz) {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(key);
        return expression.getValue(context, clazz);
    }

    /**
     * Get parameter container.
     *
     * @param arguments       the parameter list of the method
     * @param signatureMethod the body of the method being executed
     * @return container for loading parameters
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
    