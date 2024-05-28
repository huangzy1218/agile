package com.agile.gateway.config;

import com.agile.gateway.register.SwaggerDocRegister;
import com.alibaba.nacos.common.notify.NotifyCenter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Doc configuration class, Swagger 3.0.
 *
 * @author Huang Z.Y.
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "springdoc.api-docs.enabled", matchIfMissing = true)
public class SpringDocConfiguration implements InitializingBean {

    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    private final DiscoveryClient discoveryClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        NotifyCenter.registerSubscriber(new SwaggerDocRegister(swaggerUiConfigProperties, discoveryClient));
    }

}
    