package com.agile.common.security.component;

import cn.hutool.core.util.ReUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.agile.common.security.annotation.Inner;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Configuration properties class to define URLs to ignore for OAuth2 security.<br/>
 * This class is responsible for populating a list of URLs to ignore from the application properties, like permit list.
 *
 * @author Huang Z.Y.
 */
@ConfigurationProperties(prefix = "security.oauth2.ignore")
public class PermitAllUrlProperties implements InitializingBean {

    /**
     * Regular expression pattern to match path variables in URL patterns.
     */
    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    /**
     * Default URLs to ignore for OAuth2 security.
     */
    private static final String[] DEFAULT_IGNORE_URLS = new String[]{"/actuator/**", "/error", "/v3/api-docs"};

    /**
     * List of URLs to ignore for OAuth2 security.
     */
    @Getter
    @Setter
    private List<String> urls = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        urls.addAll(Arrays.asList(DEFAULT_IGNORE_URLS));
        RequestMappingHandlerMapping mapping = SpringUtil.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = map.get(info);

            // Replace the path variable with *
            Inner method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Inner.class);
            Optional.ofNullable(method)
                    .ifPresent(inner -> Objects.requireNonNull(info.getPathPatternsCondition())
                            .getPatternValues()
                            .forEach(url -> urls.add(ReUtil.replaceAll(url, PATTERN, "*"))));

            // Replace the path variable with *
            Inner controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Inner.class);
            Optional.ofNullable(controller)
                    .ifPresent(inner -> Objects.requireNonNull(info.getPathPatternsCondition())
                            .getPatternValues()
                            .forEach(url -> urls.add(ReUtil.replaceAll(url, PATTERN, "*"))));
        });
    }

}
    