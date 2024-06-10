package com.agile.common.security.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configure authentication and authorization for resource servers.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class AgileResourceServerConfiguration {

    protected final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

    private final PermitAllUrlProperties permitAllUrl;

    private final AgileBearerTokenExtractor pigBearerTokenExtractor;

    private final OpaqueTokenIntrospector customOpaqueTokenIntrospector;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AntPathRequestMatcher[] requestMatchers = permitAllUrl.getUrls()
                .stream()
                .map(AntPathRequestMatcher::new)
                .toList()
                .toArray(new AntPathRequestMatcher[]{});

        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers(requestMatchers)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(
                        oauth2 -> oauth2.opaqueToken(token -> token.introspector(customOpaqueTokenIntrospector))
                                .authenticationEntryPoint(resourceAuthExceptionEntryPoint)
                                .bearerTokenResolver(pigBearerTokenExtractor))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
    