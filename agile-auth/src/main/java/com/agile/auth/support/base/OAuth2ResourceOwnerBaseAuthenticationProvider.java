package com.agile.auth.support.base;

import cn.hutool.extra.spring.SpringUtil;
import com.agile.common.security.util.OAuth2ErrorCodesExpand;
import com.agile.common.security.util.ScopeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;

/**
 * custom authorization handler.
 *
 * @author Huang Z.Y.
 */
@Slf4j
public abstract class OAuth2ResourceOwnerBaseAuthenticationProvider<T extends OAuth2ResourceOwnerBaseAuthenticationToken>
        implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";

    private final OAuth2AuthorizationService authorizationService;

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    private final AuthenticationManager authenticationManager;

    private final MessageSourceAccessor messages;

    @Deprecated
    private Supplier<String> refreshTokenGenerator;

    /**
     * Constructs an {@code OAuth2AuthorizationCodeAuthenticationProvider} using the
     * provided parameters.
     *
     * @param authorizationService The authorization service
     * @param tokenGenerator       The token generator
     * @since 0.2.3
     */
    public OAuth2ResourceOwnerBaseAuthenticationProvider(AuthenticationManager authenticationManager,
                                                         OAuth2AuthorizationService authorizationService,
                                                         OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authenticationManager = authenticationManager;
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;

        // International configuration
        this.messages = new MessageSourceAccessor(SpringUtil.getBean("securityMessageSource"), Locale.CHINA);
    }

    @Deprecated
    public void setRefreshTokenGenerator(Supplier<String> refreshTokenGenerator) {
        Assert.notNull(refreshTokenGenerator, "refreshTokenGenerator cannot be null");
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    public abstract UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters);

    /**
     * Whether the current provider supports this token type.
     *
     * @param authentication Authentication
     * @return {@code true} for supported
     */
    @Override
    public abstract boolean supports(Class<?> authentication);

    /**
     * Whether the current requesting client supports this mode.
     *
     * @param registeredClient Client
     */
    public abstract void checkClient(RegisteredClient registeredClient);

    /**
     * Performs authentication with the same contract as
     * {@link AuthenticationManager#authenticate(Authentication)} .
     *
     * @param authentication The authentication request object
     * @return MybatisPlusMetaObjectHandler fully authenticated object including credentials
     * @throws AuthenticationException if authentication fails.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        T resourceOwnerBaseAuthentication = (T) authentication;

        // Verify and obtain an authenticated client token
        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(
                resourceOwnerBaseAuthentication);

        // Obtain and verify the registered client information
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        checkClient(registeredClient);

        Set<String> authorizedScopes;
        // Default to configured scopes
        if (!CollectionUtils.isEmpty(resourceOwnerBaseAuthentication.getScopes())) {
            for (String requestedScope : resourceOwnerBaseAuthentication.getScopes()) {
                assert registeredClient != null;
                if (!registeredClient.getScopes().contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(resourceOwnerBaseAuthentication.getScopes());
        } else {
            authorizedScopes = new LinkedHashSet<>();
        }

        Map<String, Object> reqParameters = resourceOwnerBaseAuthentication.getAdditionalParameters();
        try {
            // Build user name and password authentication tokens
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = buildToken(reqParameters);

            log.debug("got usernamePasswordAuthenticationToken=" + usernamePasswordAuthenticationToken);

            // Perform user name and password authentication
            Authentication usernamePasswordAuthentication = authenticationManager
                    .authenticate(usernamePasswordAuthenticationToken);

            // @formatter:off
            // Build the token context
            DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                    .registeredClient(registeredClient)
                    .principal(usernamePasswordAuthentication)
                    .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                    .authorizedScopes(authorizedScopes)
                    .authorizationGrantType(resourceOwnerBaseAuthentication.getAuthorizationGrantType())
                    .authorizationGrant(resourceOwnerBaseAuthentication);
            // @formatter:on

            OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                    .withRegisteredClient(registeredClient)
                    .principalName(usernamePasswordAuthentication.getName())
                    .authorizationGrantType(resourceOwnerBaseAuthentication.getAuthorizationGrantType())
                    // 0.4.0 New method
                    .authorizedScopes(authorizedScopes);

            // ----- Access token -----
            // Generate access tokens
            OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
            OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
            if (generatedAccessToken == null) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the access token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }
            OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                    generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                    generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
            if (generatedAccessToken instanceof ClaimAccessor) {
                authorizationBuilder.id(accessToken.getTokenValue())
                        .token(accessToken,
                                (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                                        ((ClaimAccessor) generatedAccessToken).getClaims()))
                        // 0.4.0 New method
                        .authorizedScopes(authorizedScopes)
                        .attribute(Principal.class.getName(), usernamePasswordAuthentication);
            } else {
                authorizationBuilder.id(accessToken.getTokenValue()).accessToken(accessToken);
            }

            // ----- Refresh token -----
            // If the client supports refresh tokens, generate refresh tokens
            OAuth2RefreshToken refreshToken = null;
            if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                    // Do not issue refresh token to public client
                    !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

                if (this.refreshTokenGenerator != null) {
                    Instant issuedAt = Instant.now();
                    Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getRefreshTokenTimeToLive());
                    refreshToken = new OAuth2RefreshToken(this.refreshTokenGenerator.get(), issuedAt, expiresAt);
                } else {
                    tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
                    OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
                    if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                        OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                                "The token generator failed to generate the refresh token.", ERROR_URI);
                        throw new OAuth2AuthenticationException(error);
                    }
                    refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
                }
                authorizationBuilder.refreshToken(refreshToken);
            }

            // Save authorization information
            OAuth2Authorization authorization = authorizationBuilder.build();

            this.authorizationService.save(authorization);

            log.debug("Returning OAuth2AccessTokenAuthenticationToken");

            return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken,
                    refreshToken, Objects.requireNonNull(authorization.getAccessToken().getClaims()));

        } catch (Exception ex) {
            log.error("Problem in authenticate", ex);
            throw oAuth2AuthenticationException(authentication, (AuthenticationException) ex);
        }

    }

    /**
     * The login exception translates to an oauth 2 exception.
     *
     * @param authentication          Identity authentication
     * @param authenticationException Authentication exception
     * @return {@link OAuth2AuthenticationException}
     */
    private OAuth2AuthenticationException oAuth2AuthenticationException(Authentication authentication,
                                                                        AuthenticationException authenticationException) {
        if (authenticationException instanceof UsernameNotFoundException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USERNAME_NOT_FOUND,
                    this.messages.getMessage("JdbcDaoImpl.notFound", new Object[]{authentication.getName()},
                            "Username {0} not found"),
                    ""));
        }
        if (authenticationException instanceof BadCredentialsException) {
            return new OAuth2AuthenticationException(
                    new OAuth2Error(OAuth2ErrorCodesExpand.BAD_CREDENTIALS, this.messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"), ""));
        }
        if (authenticationException instanceof LockedException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USER_LOCKED, this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"), ""));
        }
        if (authenticationException instanceof DisabledException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USER_DISABLE,
                    this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"),
                    ""));
        }
        if (authenticationException instanceof AccountExpiredException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USER_EXPIRED, this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"), ""));
        }
        if (authenticationException instanceof CredentialsExpiredException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.CREDENTIALS_EXPIRED,
                    this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                            "User credentials have expired"),
                    ""));
        }
        if (authenticationException instanceof ScopeException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_SCOPE,
                    this.messages.getMessage("AbstractAccessDecisionManager.accessDenied", "invalid_scope"), ""));
        }
        return new OAuth2AuthenticationException(OAuth2ErrorCodesExpand.UN_KNOW_LOGIN_ERROR);
    }

    /**
     * Retrieves and validates the authenticated client from the given {@link Authentication} object.
     */
    private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
            Authentication authentication) {

        OAuth2ClientAuthenticationToken clientPrincipal = null;

        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }

        // Return the client principal if it is authenticated
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }

        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

}
    