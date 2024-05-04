package com.agile.common.log;

import com.agile.common.log.aspect.LogAspect;
import com.agile.common.log.config.AgileLogProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 日志自动配置。
 *
 * @author Huang Z.Y.
 */
@EnableAsync
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AgileLogProperties.class)
@ConditionalOnProperty(value = "security.log.enabled", matchIfMissing = true)
public class LogAutoConfig {

    @Bean
    public LogListener logListener(AgileLogProperties logProperties, RemoteLogService remoteLogService) {
        return new LogListener(remoteLogService, logProperties);
    }

    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}
    