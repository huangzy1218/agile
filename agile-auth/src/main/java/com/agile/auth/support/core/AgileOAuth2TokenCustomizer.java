package com.agile.auth.support.core;

import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.security.service.AgileUser;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * Token output enhancement.
 *
 * @author Huang Z.Y.
 */
public class AgileOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    /**
     * Customize the OAuth 2.0 Token attributes.
     *
     * @param context the context containing the OAuth 2.0 Token attributes
     */
    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        OAuth2TokenClaimsSet.Builder claims = context.getClaims();
        String clientId = context.getAuthorizationGrant().getName();
        claims.claim(SecurityConstants.CLIENT_ID, clientId);
        // Client mode does not return specific user information
        if (SecurityConstants.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType().getValue())) {
            return;
        }

        AgileUser pigUser = (AgileUser) context.getPrincipal().getPrincipal();
        claims.claim(SecurityConstants.DETAILS_USER, pigUser);
        claims.claim(SecurityConstants.DETAILS_USER_ID, pigUser.getId());
        claims.claim(SecurityConstants.USERNAME, pigUser.getUsername());
    }

}
    