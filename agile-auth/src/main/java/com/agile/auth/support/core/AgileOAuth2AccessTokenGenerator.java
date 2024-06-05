package com.agile.auth.support.core;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;

/**
 * Custom {@link OAuth2TokenGenerator}, generating Oauth2 access token.
 *
 * @author Huang Z.Y.
 */
public class AgileOAuth2AccessTokenGenerator implements OAuth2TokenGenerator<OAuth2AccessToken> {

    private OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer;

    /**
     * Customizer for modifying the access token claims.
     */
    private final StringKeyGenerator accessTokenGenerator = new Base64StringKeyGenerator(
            Base64.getUrlEncoder().withoutPadding(), 96);

    /**
     * Generates an Oauth2 access token based on the provided context.
     *
     * @param context Oauth2 token context
     * @return The generated Oauth2 Access token
     */
    @Nullable
    @Override
    public OAuth2AccessToken generate(OAuth2TokenContext context) {
        // Check if the token type11 is ACCESS_TOKEN and the token format is REFERENCE
        if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) || !OAuth2TokenFormat.REFERENCE
                .equals(context.getRegisteredClient().getTokenSettings().getAccessTokenFormat())) {
            return null;
        }

        // Retrieve the issuer from the context
        String issuer = null;
        if (context.getAuthorizationServerContext() != null) {
            issuer = context.getAuthorizationServerContext().getIssuer();
        }
        RegisteredClient registeredClient = context.getRegisteredClient();

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getAccessTokenTimeToLive());

        // Build the token claims set
        OAuth2TokenClaimsSet.Builder claimsBuilder = OAuth2TokenClaimsSet.builder();
        if (StringUtils.hasText(issuer)) {
            claimsBuilder.issuer(issuer);
        }
        claimsBuilder
                .subject(context.getPrincipal().getName())
                .audience(Collections.singletonList(registeredClient.getClientId()))
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .notBefore(issuedAt)
                .id(UUID.randomUUID().toString());
        if (!CollectionUtils.isEmpty(context.getAuthorizedScopes())) {
            claimsBuilder.claim(OAuth2ParameterNames.SCOPE, context.getAuthorizedScopes());

            if (this.accessTokenCustomizer != null) {
                OAuth2TokenClaimsContext.Builder accessTokenContextBuilder = OAuth2TokenClaimsContext.with(claimsBuilder)
                        .registeredClient(context.getRegisteredClient())
                        .principal(context.getPrincipal())
                        .authorizationServerContext(context.getAuthorizationServerContext())
                        .authorizedScopes(context.getAuthorizedScopes())
                        .tokenType(context.getTokenType())
                        .authorizationGrantType(context.getAuthorizationGrantType());
                if (context.getAuthorization() != null) {
                    accessTokenContextBuilder.authorization(context.getAuthorization());
                }
                if (context.getAuthorizationGrant() != null) {
                    accessTokenContextBuilder.authorizationGrant(context.getAuthorizationGrant());
                }

                OAuth2TokenClaimsContext accessTokenContext = accessTokenContextBuilder.build();
                this.accessTokenCustomizer.customize(accessTokenContext);
            }
        }

        // Build the final access token claims set
        OAuth2TokenClaimsSet accessTokenClaimsSet = claimsBuilder.build();
        return new AgileOAuth2AccessTokenGenerator.OAuth2AccessTokenClaims(OAuth2AccessToken.TokenType.BEARER,
                this.accessTokenGenerator.generateKey(), accessTokenClaimsSet.getIssuedAt(),
                accessTokenClaimsSet.getExpiresAt(), context.getAuthorizedScopes(), accessTokenClaimsSet.getClaims());
    }

    /**
     * Sets the {@link OAuth2TokenCustomizer} that customizes the
     * {@link OAuth2TokenClaimsContext#getClaims() claims} for the
     * {@link OAuth2AccessToken}.
     *
     * @param accessTokenCustomizer The {@link OAuth2TokenCustomizer} that customizes the
     *                              claims for the {@code OAuth2AccessToken}
     */
    public void setAccessTokenCustomizer(OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer) {
        Assert.notNull(accessTokenCustomizer, "accessTokenCustomizer cannot be null");
        this.accessTokenCustomizer = accessTokenCustomizer;
    }

    /**
     * The OAuth2AccessTokenClaims class represents an OAuth2 access token with associated claims.
     */
    private static final class OAuth2AccessTokenClaims extends OAuth2AccessToken implements ClaimAccessor {

        private final Map<String, Object> claims;

        /**
         * Constructs an OAuth2AccessTokenClaims instance.
         *
         * @param tokenType  The type of the token
         * @param tokenValue The value of the token
         * @param issuedAt   The time the token was issued
         * @param expiresAt  The time the token expires
         * @param scopes     The scopes associated with the token
         * @param claims     The claims associated with the token
         */
        private OAuth2AccessTokenClaims(TokenType tokenType, String tokenValue, Instant issuedAt, Instant expiresAt,
                                        Set<String> scopes, Map<String, Object> claims) {
            super(tokenType, tokenValue, issuedAt, expiresAt, scopes);
            this.claims = claims;
        }

        @Override
        public Map<String, Object> getClaims() {
            return this.claims;
        }

    }

}
    