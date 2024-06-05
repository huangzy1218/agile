package com.agile.auth.config;

import com.agile.auth.handler.AgileAuthenticationFailureEventHandler;
import com.agile.auth.handler.AgileAuthenticationSuccessEventHandler;
import com.agile.auth.support.core.AgileOAuth2AccessTokenGenerator;
import com.agile.auth.support.core.AgileOAuth2TokenCustomizer;
import com.agile.auth.support.core.FormIdentityLoginConfigurer;
import com.agile.auth.support.filter.PasswordDecoderFilter;
import com.agile.auth.support.filter.ValidateCodeFilter;
import com.agile.auth.support.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.agile.auth.support.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import com.agile.auth.support.sms.OAuth2ResourceOwnerSmsAuthenticationConverter;
import com.agile.auth.support.sms.OAuth2ResourceOwnerSmsAuthenticationProvider;
import com.agile.common.core.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

/**
 * Authentication server configuration.
 *
 * @author Huang Z.Y.
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class AuthorizationServerConfiguration {

    private final OAuth2AuthorizationService authorizationService;

    private final PasswordDecoderFilter passwordDecoderFilter;

    private final ValidateCodeFilter validateCodeFilter;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnProperty(value = "security.micro", matchIfMissing = true)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        // Add a validate code filter
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
        // Add a password decoder filter
        http.addFilterBefore(passwordDecoderFilter, UsernamePasswordAuthenticationFilter.class);

        // Personalized authentication and authorization endpoint
        http.with(authorizationServerConfigurer.tokenEndpoint((tokenEndpoint) -> {
                    // Inject a custom authentication Converter
                    tokenEndpoint.accessTokenRequestConverter(accessTokenRequestConverter())
                            // Login success processor
                            .accessTokenResponseHandler(new AgileAuthenticationSuccessEventHandler())
                            // Login failure handler
                            .errorResponseHandler(new AgileAuthenticationFailureEventHandler());
                    // Personalized client authentication
                }).clientAuthentication(oAuth2ClientAuthenticationConfigurer ->
                        // Handle client authentication exception
                        oAuth2ClientAuthenticationConfigurer.errorResponseHandler(new AgileAuthenticationFailureEventHandler()))
                // Authorization code endpoint personalization confirm page
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                        .consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI)), Customizer.withDefaults());

        // Define request matchers for endpoints that should be permitted without authentication
        AntPathRequestMatcher[] requestMatchers = new AntPathRequestMatcher[]{
                AntPathRequestMatcher.antMatcher("/token/**"), AntPathRequestMatcher.antMatcher("/actuator/**"),
                AntPathRequestMatcher.antMatcher("/code/image"), AntPathRequestMatcher.antMatcher("/css/**"),
                AntPathRequestMatcher.antMatcher("/error")};

        // Configure HTTP request authorization
        http.authorizeHttpRequests(authorizeRequests -> {
                    // Custom interface, endpoint exposure
                    authorizeRequests.requestMatchers(requestMatchers).permitAll();
                    authorizeRequests.anyRequest().authenticated();
                })
                // Implementation of redis storage token
                .with(authorizationServerConfigurer.authorizationService(authorizationService)
                                .authorizationServerSettings(
                                        AuthorizationServerSettings.builder().build()),
                        Customizer.withDefaults());
        // Configure form-based login
        http.with(new FormIdentityLoginConfigurer(), Customizer.withDefaults());
        DefaultSecurityFilterChain securityFilterChain = http.build();

        // Inject custom OAuth2 grant authentication providers
        addCustomOAuth2GrantAuthenticationProvider(http);

        return securityFilterChain;
    }

    /**
     * Token generation rule implementation.<br/>
     * client:username:uuid
     *
     * @return OAuth2TokenGenerator
     */
    @Bean
    public OAuth2TokenGenerator oAuth2TokenGenerator() {
        AgileOAuth2AccessTokenGenerator accessTokenGenerator = new AgileOAuth2AccessTokenGenerator();
        // Add a Token adds the associated user information
        accessTokenGenerator.setAccessTokenCustomizer(new AgileOAuth2TokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
    }

    /**
     * request -> xToken InjectionRequestConverter
     *
     * @return DelegatingAuthenticationConverter
     */
    @Bean
    public AuthenticationConverter accessTokenRequestConverter() {
        return new DelegatingAuthenticationConverter(Arrays.asList(
                new OAuth2ResourceOwnerPasswordAuthenticationConverter(),
                new OAuth2ResourceOwnerSmsAuthenticationConverter(), new OAuth2RefreshTokenAuthenticationConverter(),
                new OAuth2ClientCredentialsAuthenticationConverter(),
                new OAuth2AuthorizationCodeAuthenticationConverter(),
                new OAuth2AuthorizationCodeRequestAuthenticationConverter()));
    }

    /**
     * Inject the authorization pattern implementation provider.<br/>
     * 1. Password mode</br>
     * 2. SMS mode</br>
     */
    @SuppressWarnings("unchecked")
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new OAuth2ResourceOwnerPasswordAuthenticationProvider(
                authenticationManager, authorizationService, oAuth2TokenGenerator());

        OAuth2ResourceOwnerSmsAuthenticationProvider resourceOwnerSmsAuthenticationProvider = new OAuth2ResourceOwnerSmsAuthenticationProvider(
                authenticationManager, authorizationService, oAuth2TokenGenerator());

        // Handle OAuth2ResourceOwnerPasswordAuthenticationToken
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
        // Handle OAuth2ResourceOwnerSmsAuthenticationToken
        http.authenticationProvider(resourceOwnerSmsAuthenticationProvider);
    }

}
    