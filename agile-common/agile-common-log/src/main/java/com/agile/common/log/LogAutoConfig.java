package com.agile.common.log;

import com.agile.admin.api.feign.RemoteLogService;
import com.agile.common.log.aspect.SysLogAspect;
import com.agile.common.log.config.AgileLogProperties;
import com.agile.common.log.event.SysLogListener;
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
    public SysLogListener logListener(AgileLogProperties logProperties, RemoteLogService remoteLogService) {
        return new SysLogListener(remoteLogService, logProperties);
    }

    @Bean
    public SysLogAspect logAspect() {
        return new SysLogAspect();
    }
}
    