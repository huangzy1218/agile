package com.agile.common.security.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

/**
 * @author Huang Z.Y.
 */
public class AgileFeignClientConfiguration {

    /**
     * Inject oauth2 feign token enhancement.
     *
     * @param tokenResolver Token acquisition processor
     * @return Interceptor
     */
    @Bean
    public RequestInterceptor oauthRequestInterceptor(BearerTokenResolver tokenResolver) {
        return new AgileOAuthRequestInterceptor(tokenResolver);
    }

}
    