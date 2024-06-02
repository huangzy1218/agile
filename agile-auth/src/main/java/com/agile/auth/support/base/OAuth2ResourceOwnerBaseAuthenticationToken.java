package com.agile.auth.support.base;

import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Custom authorization schema abstraction.
 *
 * @author Huang Z.Y.
 */
public abstract class OAuth2ResourceOwnerBaseAuthenticationToken
        extends AbstractAuthenticationToken {

    @Getter
    private final AuthorizationGrantType authorizationGrantType;

    @Getter
    private final Authentication clientPrincipal;

    @Getter
    private final Set<String> scopes;

    @Getter
    private final Map<String, Object> additionalParameters;

    public OAuth2ResourceOwnerBaseAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                                      Authentication clientPrincipal, @Nullable Set<String> scopes,
                                                      @Nullable Map<String, Object> additionalParameters) {
        super(Collections.emptyList());
        Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
        Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
        this.authorizationGrantType = authorizationGrantType;
        this.clientPrincipal = clientPrincipal;
        this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
        this.additionalParameters = Collections.unmodifiableMap(
                additionalParameters != null ? new HashMap<>(additionalParameters) : Collections.emptyMap());
    }

    /**
     * Extended mode generally does not require a password.
     */
    @Override
    public Object getCredentials() {
        return "";
    }

    /**
     * Gets username (principal).
     */
    @Override
    public Object getPrincipal() {
        return this.clientPrincipal;
    }

}
    