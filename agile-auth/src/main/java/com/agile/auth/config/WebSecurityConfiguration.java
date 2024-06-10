package com.agile.auth.config;

import com.agile.auth.support.core.AgileDaoAuthenticationProvider;
import com.agile.auth.support.core.FormIdentityLoginConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Service security-related configuration.
 *
 * @author Huang Z.Y.
 */
@EnableWebSecurity
public class WebSecurityConfiguration {

    /**
     * Spring security Default security policy.
     *
     * @param http Security injection point
     * @return SecurityFilterChain
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/token/*")
                .permitAll()// Open custom partial endpoints
                .anyRequest()
                .authenticated()).headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)// 避免iframe同源无法登录许iframe
        ).with(new FormIdentityLoginConfigurer(), Customizer.withDefaults()); // 表单登录个性化
        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new AgileDaoAuthenticationProvider());
        return http.build();
    }

    /**
     * Expose static resources.
     *
     * @param http Security injection point
     */
    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        http.securityMatchers((matchers) -> matchers.requestMatchers("/actuator/**", "/css/**", "/error"))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
                .requestCache(RequestCacheConfigurer::disable)
                .securityContext(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable);
        return http.build();
    }

}
    