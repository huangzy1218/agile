package com.agile.auth.support.password;

import com.agile.auth.support.base.OAuth2ResourceOwnerBaseAuthenticationConverter;
import com.agile.common.security.util.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * Cryptographic authentication converter.
 *
 * @author Huang Z.Y.
 */
public class OAuth2ResourceOwnerPasswordAuthenticationConverter
        extends OAuth2ResourceOwnerBaseAuthenticationConverter<OAuth2ResourceOwnerPasswordAuthenticationToken> {

    /**
     * Support password mode.
     *
     * @param grantType Grant type
     */
    @Override
    public boolean support(String grantType) {
        return AuthorizationGrantType.PASSWORD.getValue().equals(grantType);
    }

    @Override
    public OAuth2ResourceOwnerPasswordAuthenticationToken buildToken(Authentication clientPrincipal,
                                                                     Set requestedScopes, Map additionalParameters) {
        return new OAuth2ResourceOwnerPasswordAuthenticationToken(AuthorizationGrantType.PASSWORD, clientPrincipal,
                requestedScopes, additionalParameters);
    }

    /**
     * Verification Extension Parameter Password Mode. The password must not be empty.
     *
     * @param request Parameter list
     */
    @Override
    public void checkParams(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        // username (REQUIRED)
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.USERNAME,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // password (REQUIRED)
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.PASSWORD,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
    }

}
    