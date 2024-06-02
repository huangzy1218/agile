package com.agile.auth.support.sms;

import com.agile.auth.support.base.OAuth2ResourceOwnerBaseAuthenticationConverter;
import com.agile.common.core.constant.SecurityConstants;
import com.agile.common.security.util.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * SMS login converter.
 *
 * @author Huang Z.Y.
 */
public class OAuth2ResourceOwnerSmsAuthenticationConverter
        extends OAuth2ResourceOwnerBaseAuthenticationConverter<OAuth2ResourceOwnerSmsAuthenticationToken> {

    /**
     * Support sms mode.
     *
     * @param grantType Grant type.
     */
    @Override
    public boolean support(String grantType) {
        return SecurityConstants.MOBILE.equals(grantType);
    }

    @Override
    public OAuth2ResourceOwnerSmsAuthenticationToken buildToken(Authentication clientPrincipal, Set requestedScopes,
                                                                Map additionalParameters) {
        return new OAuth2ResourceOwnerSmsAuthenticationToken(new AuthorizationGrantType(SecurityConstants.MOBILE),
                clientPrincipal, requestedScopes, additionalParameters);
    }

    /**
     * Verification Extension Parameter Password Mode. The mobile must not be empty.
     *
     * @param request Parameter list
     */
    @Override
    public void checkParams(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        // PHONE (REQUIRED)
        String phone = parameters.getFirst(SecurityConstants.SMS_PARAMETER_NAME);
        if (!StringUtils.hasText(phone) || parameters.get(SecurityConstants.SMS_PARAMETER_NAME).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, SecurityConstants.SMS_PARAMETER_NAME,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
    }

}
    