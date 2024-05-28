package com.agile.gateway.config;

import com.agile.gateway.filter.AgileRequestGlobalFilter;
import com.agile.gateway.handler.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway configuration.
 *
 * @author Huang Z.Y.
 */
@Configuration(proxyBeanMethods = false)
public class GatewayConfiguration {

    /**
     * Create a {@link AgileRequestGlobalFilter}.
     *
     * @return PigRequest global filter
     */
    @Bean
    public AgileRequestGlobalFilter agileRequestGlobalFilter() {
        return new AgileRequestGlobalFilter();
    }

    /**
     * Create a global exception handler.
     *
     * @param objectMapper Object mapper
     * @return Global exception handler
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
        return new GlobalExceptionHandler(objectMapper);
    }

}


    