package com.agile.common.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Huang Z.Y.
 */
public class RestTemplateConfiguration {

    /**
     * Access the template class of a Restful service
     *
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
    