package com.agile.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author Huang Z.Y.
 */
@Configuration(proxyBeanMethods = false)
public class RateLimiterConfiguration {

    /**
     * Creates a bean for resolving the key based on the remote address of the request.<br/>
     *
     * @return KeyResolver as a unique identifier
     */
    @Bean
    public KeyResolver remoteAddrKeyResolver() {
        // The remote address of the request is used as a unique identifier (key) to determine traffic limiting
        return exchange -> Mono
                .just(Objects.requireNonNull(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()))
                        .getAddress()
                        .getHostAddress());
    }

}
    