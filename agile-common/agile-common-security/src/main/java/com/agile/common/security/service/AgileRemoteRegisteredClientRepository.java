package com.agile.common.security.service;

import com.agile.admin.api.entity.SysOauthClientDetails;
import com.agile.admin.api.feign.RemoteClientDetailsService;
import com.agile.common.core.constant.CacheConstants;
import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.core.util.RetOps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.Optional;


/**
 * Query client information implementation.
 *
 * @author Huang Z.Y.
 */
@RequiredArgsConstructor
public class AgileRemoteRegisteredClientRepository implements RegisteredClientRepository {

    /**
     * Refresh token is valid for 30 days by default.
     */
    private final static int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 30;

    /**
     * Request token is valid for 12 hours by default.
     */
    private final static int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12;

    private final RemoteClientDetailsService clientDetailsService;

    /**
     * Saves the registered client.
     *
     * <p>
     * IMPORTANT: Sensitive information should be encoded externally from the
     * implementation, e.g. {@link RegisteredClient#getClientSecret()}
     *
     * @param registeredClient The {@link RegisteredClient}
     */
    @Override
    public void save(RegisteredClient registeredClient) {
    }

    /**
     * Returns the registered client identified by the provided {@code id}, or
     * {@code null} if not found.
     *
     * @param id The registration identifier
     * @return The {@link RegisteredClient} if found, otherwise {@code null}
     */
    @Override
    public RegisteredClient findById(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Overridden native methods support Redis caching.
     *
     * @param clientId Client ID
     * @return RegisteredClient instance
     */
    @Override
    @SneakyThrows
    @Cacheable(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
    public RegisteredClient findByClientId(String clientId) {
        SysOauthClientDetails clientDetails = RetOps
                .of(clientDetailsService.getClientDetailsById(clientId, SecurityConstants.FROM_IN))
                .getData()
                .orElseThrow(() -> new OAuth2AuthorizationCodeRequestAuthenticationException(
                        new OAuth2Error("The client query is abnormal. Please check the database link"), null));

        RegisteredClient.Builder builder = RegisteredClient.withId(clientDetails.getClientId())
                .clientId(clientDetails.getClientId())
                .clientSecret(SecurityConstants.NOOP + clientDetails.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);

        // Add authorized grant types
        for (String authorizedGrantType : clientDetails.getAuthorizedGrantTypes()) {
            builder.authorizationGrantType(new AuthorizationGrantType(authorizedGrantType));
        }

        // Build the RegisteredClient object with token
        return builder
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .accessTokenTimeToLive(Duration.ofSeconds(
                                Optional.ofNullable(clientDetails.getAccessTokenValidity()).orElse(ACCESS_TOKEN_VALIDITY_SECONDS)))
                        .refreshTokenTimeToLive(Duration.ofSeconds(Optional.ofNullable(clientDetails.getRefreshTokenValidity())
                                .orElse(REFRESH_TOKEN_VALIDITY_SECONDS)))
                        .build()).build();
    }

}
    