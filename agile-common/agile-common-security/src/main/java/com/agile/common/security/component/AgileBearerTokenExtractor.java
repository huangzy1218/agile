package com.agile.common.security.component;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for extracting bearer tokens from incoming HTTP requests
 * based on the OAuth 2.0 Bearer Token Authentication specification.
 *
 * @author Huang Z.Y.
 */
public class AgileBearerTokenExtractor implements BearerTokenResolver {

    /**
     * Regular expression pattern to match the Bearer token format in the Authorization header.
     */
    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$",
            Pattern.CASE_INSENSITIVE);

    /**
     * Flag to allow extracting bearer tokens from form-encoded body parameters.
     */
    private boolean allowFormEncodedBodyParameter = false;

    /**
     * Flag to allow extracting bearer tokens from URI query parameters.
     */
    private boolean allowUriQueryParameter = true;

    /**
     * Name of the HTTP header where the bearer token is expected.
     */
    private String bearerTokenHeaderName = HttpHeaders.AUTHORIZATION;

    /**
     * Ant-style path matcher to check if a request URI matches any configured URL patterns.
     */
    private final PathMatcher pathMatcher = new AntPathMatcher();

    /**
     * Properties containing URL patterns for which bearer token extraction should be skipped
     */
    private final PermitAllUrlProperties urlProperties;

    public AgileBearerTokenExtractor(PermitAllUrlProperties urlProperties) {
        this.urlProperties = urlProperties;
    }

    @Override
    public String resolve(HttpServletRequest request) {
        boolean match = urlProperties.getUrls()
                .stream()
                .anyMatch(url -> pathMatcher.match(url, request.getRequestURI()));

        if (match) {
            return null;
        }

        final String authorizationHeaderToken = resolveFromAuthorizationHeader(request);
        final String parameterToken = isParameterTokenSupportedForRequest(request)
                ? resolveFromRequestParameters(request) : null;
        if (authorizationHeaderToken != null) {
            if (parameterToken != null) {
                final BearerTokenError error = BearerTokenErrors
                        .invalidRequest("Found multiple bearer tokens in the request");
                throw new OAuth2AuthenticationException(error);
            }
            return authorizationHeaderToken;
        }
        if (parameterToken != null && isParameterTokenEnabledForRequest(request)) {
            return parameterToken;
        }
        return null;
    }

    private String resolveFromAuthorizationHeader(HttpServletRequest request) {
        String authorization = request.getHeader(this.bearerTokenHeaderName);
        if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
            return null;
        }
        Matcher matcher = authorizationPattern.matcher(authorization);
        if (!matcher.matches()) {
            BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
            throw new OAuth2AuthenticationException(error);
        }
        return matcher.group("token");
    }

    private static String resolveFromRequestParameters(HttpServletRequest request) {
        String[] values = request.getParameterValues("access_token");
        if (values == null || values.length == 0) {
            return null;
        }
        if (values.length == 1) {
            return values[0];
        }
        BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
        throw new OAuth2AuthenticationException(error);
    }

    private boolean isParameterTokenSupportedForRequest(final HttpServletRequest request) {
        return (("POST".equals(request.getMethod())
                && MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType()))
                || "GET".equals(request.getMethod()));
    }

    private boolean isParameterTokenEnabledForRequest(final HttpServletRequest request) {
        return ((this.allowFormEncodedBodyParameter && "POST".equals(request.getMethod())
                && MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType()))
                || (this.allowUriQueryParameter && "GET".equals(request.getMethod())));
    }

}
    